package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptArgument;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example of implementing a synchronous MCP server with prompts.
 */
public class SyncServerStdioPromptsExample {

    public static void main(String[] args) throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("prompts-example-server", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create a code analysis prompt
        List<PromptArgument> codeAnalysisArgs = new ArrayList<>();
        codeAnalysisArgs.add(new PromptArgument(
            "language",
            "The programming language of the code",
            true
        ));
        codeAnalysisArgs.add(new PromptArgument(
            "code",
            "The code to analyze",
            true
        ));
        
        Prompt codeAnalysisPrompt = new Prompt(
            "code-analysis",
            "Analyzes code for potential issues and improvements",
            codeAnalysisArgs
        );
        
        // Create server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .prompts(
                new io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification(
                    codeAnalysisPrompt,
                    (exchange, request) -> {
                    // Extract arguments from the request
                    String language = (String) request.arguments().get("language");
                    String code = (String) request.arguments().get("code");
                    
                    // Create prompt messages
                    List<PromptMessage> messages = new ArrayList<>();
                    
                    // System message
                    messages.add(new PromptMessage(
                        Role.USER,
                        new TextContent("You are a code analysis assistant that helps identify issues and suggest improvements.")
                    ));
                    
                    // User message with the code
                    messages.add(new PromptMessage(
                        Role.USER,
                        new TextContent("Please analyze this " + language + " code:\n\n```" + language + "\n" + code + "\n```")
                    ));
                    
                    // Assistant message with the analysis
                    messages.add(new PromptMessage(
                        Role.ASSISTANT,
                        new TextContent("Here's my analysis of your " + language + " code:\n\n" +
                        "1. The code is very minimal and doesn't do anything yet.\n" +
                        "2. Consider adding some functionality to the main method.\n" +
                        "3. Add comments to explain the purpose of the class.")
                    ));
                    
                    // Return the prompt result
                    return new GetPromptResult(
                        "Code analysis for " + language,
                        messages
                    );
                    }
                )
            )
            .build();
            
        System.err.println("Prompts server started");
    }
}
