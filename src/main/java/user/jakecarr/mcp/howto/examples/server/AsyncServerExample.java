package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * Example of implementing an asynchronous MCP server.
 */
public class AsyncServerExample {

    public static void main(String[] args) {
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("example-server", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create server using the builder pattern
            McpAsyncServer server = McpServer.async(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "example-tool",
                        "An example tool",
                        createToolSchema()
                    ),
                    (exchange, toolArgs) -> {
                        String param1 = (String) toolArgs.get("param1");
                        Number param2 = (Number) toolArgs.get("param2");
                        
                        List<McpSchema.Content> content = new ArrayList<>();
                        content.add(new McpSchema.TextContent(
                            null,
                            null,
                            "Tool executed with param1=" + param1 + ", param2=" + param2
                        ));
                        
                        return Mono.just(new McpSchema.CallToolResult(content, false));
                    }
                )
                .build();
                
            System.err.println("Server started");
        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
    
    /**
     * Creates the JSON schema for the example tool.
     */
    private static McpSchema.JsonSchema createToolSchema() {
        // Create input schema for the tool
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> param1 = new HashMap<>();
        param1.put("type", "string");
        param1.put("description", "A string parameter");
        
        Map<String, Object> param2 = new HashMap<>();
        param2.put("type", "number");
        param2.put("description", "A numeric parameter");
        
        properties.put("param1", param1);
        properties.put("param2", param2);
        
        List<String> required = List.of("param1");
        
        return new McpSchema.JsonSchema("object", properties, required, null);
    }
}
