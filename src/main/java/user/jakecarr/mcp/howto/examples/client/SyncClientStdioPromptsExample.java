package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.ListPromptsResult;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example of using the SyncClient with Stdio transport to interact with prompts from an MCP server.
 */
public class SyncClientStdioPromptsExample {
    public static void main(String[] args) throws Exception {
        // Create client info
        McpSchema.Implementation clientInfo = new McpSchema.Implementation("example-client", "1.0.0");
        
        // Create server parameters
        ServerParameters serverParams = ServerParameters.builder("example-server-command")
            .build();
        
        // Create transport with server parameters
        StdioClientTransport transport = new StdioClientTransport(serverParams);
        
        // Create the client using the builder pattern
        McpSyncClient client = McpClient.sync(transport)
            .clientInfo(clientInfo)
            .build();
        
        try {
            // List available prompts
            ListPromptsResult promptsResult = client.listPrompts();
            
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
                GetPromptResult promptResult = client.getPrompt(promptRequest);
                
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
        } finally {
            // Close the client
            client.close();
        }
    }
}
