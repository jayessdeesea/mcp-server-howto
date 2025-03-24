package user.jakecarr.mcp.howto.examples.transport;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Example of using StdioTransport for MCP communication.
 * 
 * This example demonstrates how to set up both client and server sides
 * using standard input/output streams for communication.
 */
public class StdioTransportExample {

    /**
     * Example of setting up a server with StdioServerTransportProvider.
     */
    public static void serverExample() throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("stdio-server-example", "1.0.0");
        
        // Create transport provider for stdio
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .tool(
                new McpSchema.Tool(
                    "echo",
                    "Echoes back the input text",
                    new McpSchema.JsonSchema(
                        "object",
                        java.util.Map.of(
                            "text", java.util.Map.of(
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
            .build();
            
        System.err.println("Stdio server started");
    }
    
    /**
     * Example of setting up a client with StdioClientTransport.
     */
    public static void clientExample() throws Exception {
        // Create client info
        McpSchema.Implementation clientInfo = new McpSchema.Implementation("stdio-client-example", "1.0.0");
        
        // Create server parameters
        // In a real scenario, this would be the command to start the server process
        ServerParameters serverParams = ServerParameters.builder("example-server-command")
            .build();
        
        // Create transport with server parameters
        StdioClientTransport transport = new StdioClientTransport(serverParams);
        
        // Create the client using the builder pattern
        McpSyncClient client = McpClient.sync(transport)
            .clientInfo(clientInfo)
            .build();
        
        try {
            // Call the echo tool
            McpSchema.CallToolRequest toolRequest = new McpSchema.CallToolRequest(
                "echo", 
                java.util.Map.of("text", "Hello from StdioTransport!")
            );
            
            McpSchema.CallToolResult toolResponse = client.callTool(toolRequest);
            
            // Access the tool response content
            if (toolResponse.content() != null && !toolResponse.content().isEmpty()) {
                McpSchema.Content content = toolResponse.content().get(0);
                if (content instanceof McpSchema.TextContent textContent) {
                    System.out.println("Tool response: " + textContent.text());
                }
            }
        } finally {
            // Close the client
            client.close();
        }
    }
    
    /**
     * Main method to demonstrate the examples.
     * Note: In a real scenario, the server and client would typically be in separate processes.
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0 && args[0].equals("server")) {
                serverExample();
            } else {
                System.out.println("This example demonstrates how to use StdioTransport.");
                System.out.println("In a real scenario, the server and client would be in separate processes.");
                System.out.println("Run with 'server' argument to start the server example.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
