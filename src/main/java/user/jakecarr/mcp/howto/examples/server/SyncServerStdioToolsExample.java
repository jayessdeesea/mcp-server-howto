package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Example of implementing tools in an MCP server.
 */
public class SyncServerStdioToolsExample {

    
    public static void main(String[] args) throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("tools-example", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create the server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .tool(
                new McpSchema.Tool(
                    "echo",
                    "Echoes back the input text",
                    new McpSchema.JsonSchema(
                        "object",
                        Map.of(
                            "text", Map.of(
                                "type", "string",
                                "description", "Text to echo back"
                            )
                        ),
                        List.of("text"),
                        null
                    )
                ),
                (exchange, toolArgs) -> {
                    String text = (String) toolArgs.get("text");
                    
                    List<McpSchema.Content> content = new ArrayList<>();
                    content.add(new McpSchema.TextContent(
                        null,
                        null,
                        text
                    ));
                    
                    return new McpSchema.CallToolResult(content, false);
                }
            )
            .tool(
                new McpSchema.Tool(
                    "calculate",
                    "Performs a simple calculation",
                    new McpSchema.JsonSchema(
                        "object",
                        Map.of(
                            "operation", Map.of(
                                "type", "string",
                                "enum", List.of("add", "subtract", "multiply", "divide"),
                                "description", "Operation to perform"
                            ),
                            "a", Map.of(
                                "type", "number",
                                "description", "First operand"
                            ),
                            "b", Map.of(
                                "type", "number",
                                "description", "Second operand"
                            )
                        ),
                        List.of("operation", "a", "b"),
                        null
                    )
                ),
                (exchange, toolArgs) -> {
                    String operation = (String) toolArgs.get("operation");
                    double a = ((Number) toolArgs.get("a")).doubleValue();
                    double b = ((Number) toolArgs.get("b")).doubleValue();
                    
                    double result;
                    switch (operation) {
                        case "add":
                            result = a + b;
                            break;
                        case "subtract":
                            result = a - b;
                            break;
                        case "multiply":
                            result = a * b;
                            break;
                        case "divide":
                            if (b == 0) {
                                throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                                    McpSchema.ErrorCodes.INVALID_PARAMS,
                                    "Cannot divide by zero",
                                    null
                                ));
                            }
                            result = a / b;
                            break;
                        default:
                            throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                                McpSchema.ErrorCodes.INVALID_PARAMS,
                                "Unknown operation: " + operation,
                                null
                            ));
                    }
                    
                    List<McpSchema.Content> content = new ArrayList<>();
                    content.add(new McpSchema.TextContent(
                        null,
                        null,
                        String.valueOf(result)
                    ));
                    
                    return new McpSchema.CallToolResult(content, false);
                }
            )
            .tool(
                new McpSchema.Tool(
                    "generate-text",
                    "Generates text based on a prompt",
                    new McpSchema.JsonSchema(
                        "object",
                        Map.of(
                            "prompt", Map.of(
                                "type", "string",
                                "description", "Prompt for text generation"
                            ),
                            "length", Map.of(
                                "type", "integer",
                                "description", "Maximum length of generated text"
                            )
                        ),
                        List.of("prompt"),
                        null
                    )
                ),
                (exchange, toolArgs) -> {
                    String prompt = (String) toolArgs.get("prompt");
                    int length = toolArgs.containsKey("length") ? ((Number) toolArgs.get("length")).intValue() : 100;
                    
                    // In a real implementation, this would use an LLM or other text generation method
                    String generatedText = "Generated text based on prompt: " + prompt;
                    
                    List<McpSchema.Content> content = new ArrayList<>();
                    content.add(new McpSchema.TextContent(
                        null,
                        null,
                        generatedText.substring(0, Math.min(generatedText.length(), length))
                    ));
                    
                    return new McpSchema.CallToolResult(content, false);
                }
            )
            .build();
        
        // The server will now handle requests until the transport is closed
        System.out.println("Tools server running...");
    }
    
}
