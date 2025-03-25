package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.ListPromptsResult;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpError;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of using the AsyncClient to interact with prompts from an MCP server.
 */
public class AsyncClientStdioPromptsExample {

    public static void main(String[] args) throws Exception {
        // Create client info
        McpSchema.Implementation clientInfo = new McpSchema.Implementation("example-client", "1.0.0");
        
        // Create server parameters
        ServerParameters serverParams = ServerParameters.builder("example-server-command")
            .build();
        
        // Create transport with server parameters
        StdioClientTransport transport = new StdioClientTransport(serverParams);
        
        // Create the client using the builder pattern
        McpAsyncClient client = McpClient.async(transport)
            .clientInfo(clientInfo)
            .build();
        
        try {
            // Initialize the client (connects to the server)
            client.initialize().block(); // Block until initialization completes
            
            // List available prompts
            ListPromptsResult promptsResult = client.listPrompts().block();
            
            if (promptsResult.prompts() != null && !promptsResult.prompts().isEmpty()) {
                System.out.println("Available prompts:");
                for (Prompt prompt : promptsResult.prompts()) {
                    System.out.println("- " + prompt.name() + ": " + prompt.description());
                }
                
                // Get a specific prompt
                String promptName = promptsResult.prompts().get(0).name();
                
                // Create arguments for the prompt if needed
                Map<String, Object> promptArgs = new HashMap<>();
                promptArgs.put("language", "Java");
                promptArgs.put("code", "public class Example { public static void main(String[] args) { } }");
                
                GetPromptRequest promptRequest = new GetPromptRequest(promptName, promptArgs);
                GetPromptResult promptResult = client.getPrompt(promptRequest).block();
                
                // Process the prompt result
                if (promptResult.messages() != null && !promptResult.messages().isEmpty()) {
                    System.out.println("Prompt messages:");
                    for (PromptMessage message : promptResult.messages()) {
                        System.out.println("Role: " + message.role());
                        System.out.println("Content: " + message.content());
                    }
                }
            } else {
                System.out.println("No prompts available from the server.");
            }
        } catch (Exception e) {
            if (e.getCause() instanceof McpError) {
                McpError mcpError = (McpError) e.getCause();
                System.err.println("MCP Error: " + mcpError.getMessage());
            } else {
                System.err.println("Error: " + e.getMessage());
            }
        } finally {
            // Close the client
            client.close();
        }
    }
}
