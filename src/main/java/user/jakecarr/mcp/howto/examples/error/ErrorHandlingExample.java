package user.jakecarr.mcp.howto.examples.error;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example of error handling in MCP clients and servers.
 */
public class ErrorHandlingExample {

    public static void main(String[] args) throws Exception {
        // This example demonstrates both client-side and server-side error handling
        // In a real application, these would typically be in separate processes
        
       

        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                runServer();
            } catch (Exception e) {
                System.err.println("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        serverThread.start();
        
        // Wait for the server to start
        Thread.sleep(1000);
        
        // Run the client
        try {
            runClient();
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Runs the example server with error handling.
     */
    private static void runServer() throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("error-handling-example", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create the server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .tool(
                new McpSchema.Tool(
                    "success-tool",
                    "A tool that succeeds",
                    createToolSchema()
                ),
                (exchange, toolArgs) -> {
                    List<McpSchema.Content> content = new ArrayList<>();
                    content.add(new McpSchema.TextContent(
                        null,
                        null,
                        "Tool executed successfully"
                    ));
                    
                    return new McpSchema.CallToolResult(content, false);
                }
            )
            .tool(
                new McpSchema.Tool(
                    "not-found-tool",
                    "A tool that doesn't exist",
                    createToolSchema()
                ),
                (exchange, toolArgs) -> {
                    throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                        McpSchema.ErrorCodes.METHOD_NOT_FOUND,
                        "Tool not found: not-found-tool",
                        null
                    ));
                }
            )
            .tool(
                new McpSchema.Tool(
                    "invalid-params-tool",
                    "A tool with invalid parameters",
                    createToolSchema()
                ),
                (exchange, toolArgs) -> {
                    throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                        McpSchema.ErrorCodes.INVALID_PARAMS,
                        "Invalid parameters for tool: invalid-params-tool",
                        null
                    ));
                }
            )
            .tool(
                new McpSchema.Tool(
                    "internal-error-tool",
                    "A tool with an internal error",
                    createToolSchema()
                ),
                (exchange, toolArgs) -> {
                    throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                        McpSchema.ErrorCodes.INTERNAL_ERROR,
                        "Internal server error",
                        null
                    ));
                }
            )
            .tool(
                new McpSchema.Tool(
                    "uncaught-exception-tool",
                    "A tool with an uncaught exception",
                    createToolSchema()
                ),
                (exchange, toolArgs) -> {
                    // This will be caught by the error handler and converted to an internal error
                    throw new RuntimeException("Uncaught exception");
                }
            )
            .build();
        
        System.err.println("Server error handler: Errors will be logged automatically");
        
        System.out.println("Error handling example server running...");
    }
    
    /**
     * Creates the JSON schema for the example tools.
     */
    private static McpSchema.JsonSchema createToolSchema() {
        // Create input schema for the tool
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> param = new HashMap<>();
        param.put("type", "string");
        param.put("description", "A parameter");
        
        properties.put("param", param);
        
        List<String> required = List.of();
        
        return new McpSchema.JsonSchema("object", properties, required, null);
    }
    
    /**
     * Runs the example client with error handling.
     */
    private static void runClient() throws Exception {
        // Create client info
        McpSchema.Implementation clientInfo = new McpSchema.Implementation("error-handling-client", "1.0.0");
        
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
            // Try calling different tools to demonstrate error handling
            Map<String, Object> emptyArgs = new HashMap<>();
            
            try {
                // This should succeed
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("success-tool", emptyArgs);
                McpSchema.CallToolResult response = client.callTool(request);
                
                if (response.content() != null && !response.content().isEmpty()) {
                    McpSchema.Content content = response.content().get(0);
                    if (content instanceof McpSchema.TextContent textContent) {
                        System.out.println("Success tool response: " + textContent.text());
                    }
                }
            } catch (McpError e) {
                System.err.println("Unexpected error calling success-tool: " + e.getMessage());
            }
            
            try {
                // This should fail with METHOD_NOT_FOUND
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("not-found-tool", emptyArgs);
                McpSchema.CallToolResult response = client.callTool(request);
                System.out.println("Unexpected success calling not-found-tool");
            } catch (McpError e) {
                System.err.println("Expected error calling not-found-tool: " + e.getMessage());
                // Handle the error appropriately
                if (e.getJsonRpcError() != null && e.getJsonRpcError().code() == McpSchema.ErrorCodes.METHOD_NOT_FOUND) {
                    System.err.println("Tool not found, using fallback...");
                }
            }
            
            try {
                // This should fail with INVALID_PARAMS
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("invalid-params-tool", emptyArgs);
                McpSchema.CallToolResult response = client.callTool(request);
                System.out.println("Unexpected success calling invalid-params-tool");
            } catch (McpError e) {
                System.err.println("Expected error calling invalid-params-tool: " + e.getMessage());
                // Handle the error appropriately
                if (e.getJsonRpcError() != null && e.getJsonRpcError().code() == McpSchema.ErrorCodes.INVALID_PARAMS) {
                    System.err.println("Invalid parameters, please check your input...");
                }
            }
            
            try {
                // This should fail with INTERNAL_ERROR
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("internal-error-tool", emptyArgs);
                McpSchema.CallToolResult response = client.callTool(request);
                System.out.println("Unexpected success calling internal-error-tool");
            } catch (McpError e) {
                System.err.println("Expected error calling internal-error-tool: " + e.getMessage());
                // Handle the error appropriately
                if (e.getJsonRpcError() != null && e.getJsonRpcError().code() == McpSchema.ErrorCodes.INTERNAL_ERROR) {
                    System.err.println("Server error, please try again later...");
                }
            }
            
            try {
                // This should fail with INTERNAL_ERROR (converted from uncaught exception)
                McpSchema.CallToolRequest request = new McpSchema.CallToolRequest("uncaught-exception-tool", emptyArgs);
                McpSchema.CallToolResult response = client.callTool(request);
                System.out.println("Unexpected success calling uncaught-exception-tool");
            } catch (McpError e) {
                System.err.println("Expected error calling uncaught-exception-tool: " + e.getMessage());
                // Handle the error appropriately
                if (e.getJsonRpcError() != null && e.getJsonRpcError().code() == McpSchema.ErrorCodes.INTERNAL_ERROR) {
                    System.err.println("Server error, please try again later...");
                }
            }
            
        } finally {
            // Close the client
            client.close();
        }
    }
}
