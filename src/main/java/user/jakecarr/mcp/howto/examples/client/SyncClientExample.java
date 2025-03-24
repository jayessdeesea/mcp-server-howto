package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of using the SyncClient to interact with an MCP server.
 */
public class SyncClientExample {

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
            // Read a resource
            McpSchema.ReadResourceRequest request = new McpSchema.ReadResourceRequest("example://resource");
            McpSchema.ReadResourceResult result = client.readResource(request);
            
            // Access the resource contents
            if (result.contents() != null && !result.contents().isEmpty()) {
                McpSchema.ResourceContents contents = result.contents().get(0);
                if (contents instanceof McpSchema.TextResourceContents textContents) {
                    System.out.println("Resource content: " + textContents.text());
                }
            }
            
            // Call a tool with a Map of arguments
            Map<String, Object> toolArgs = new HashMap<>();
            toolArgs.put("param1", "value1");
            toolArgs.put("param2", 42);
            
            McpSchema.CallToolRequest toolRequest = new McpSchema.CallToolRequest("example-tool", toolArgs);
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
}
