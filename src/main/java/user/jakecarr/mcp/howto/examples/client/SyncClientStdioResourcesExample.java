package user.jakecarr.mcp.howto.examples.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpError;

import java.util.List;

/**
 * Example of a synchronous MCP client that accesses resources from a server.
 * 
 * This example demonstrates how to create a synchronous client that connects to an MCP server
 * over stdio and accesses both static resources and dynamic resources using templates.
 */
public class SyncClientStdioResourcesExample {
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
            // List available resources
            McpSchema.ListResourcesResult resourcesResult = client.listResources();
            
            if (resourcesResult.resources() != null && !resourcesResult.resources().isEmpty()) {
                System.out.println("Available resources:");
                for (McpSchema.Resource resource : resourcesResult.resources()) {
                    System.out.println("- " + resource.uri() + ": " + resource.name());
                }
                
                // Read a static resource
                String staticResourceUri = resourcesResult.resources().get(0).uri();
                McpSchema.ReadResourceRequest staticRequest = new McpSchema.ReadResourceRequest(staticResourceUri);
                McpSchema.ReadResourceResult staticResult = client.readResource(staticRequest);
                
                // Process the resource contents
                if (staticResult.contents() != null && !staticResult.contents().isEmpty()) {
                    McpSchema.ResourceContents contents = staticResult.contents().get(0);
                    if (contents instanceof McpSchema.TextResourceContents textContents) {
                        System.out.println("Static resource content: " + textContents.text());
                    }
                }
            }
            
            // List available resource templates
            McpSchema.ListResourceTemplatesResult templatesResult = client.listResourceTemplates();
            
            if (templatesResult.resourceTemplates() != null && !templatesResult.resourceTemplates().isEmpty()) {
                System.out.println("Available resource templates:");
                for (McpSchema.ResourceTemplate template : templatesResult.resourceTemplates()) {
                    System.out.println("- " + template.uriTemplate() + ": " + template.name());
                }
                
                // Read a dynamic resource using a template
                // For example, if there's a template like "data://users/{userId}"
                String dynamicResourceUri = "data://users/123";
                McpSchema.ReadResourceRequest dynamicRequest = new McpSchema.ReadResourceRequest(dynamicResourceUri);
                McpSchema.ReadResourceResult dynamicResult = client.readResource(dynamicRequest);
                
                // Process the resource contents
                if (dynamicResult.contents() != null && !dynamicResult.contents().isEmpty()) {
                    McpSchema.ResourceContents contents = dynamicResult.contents().get(0);
                    if (contents instanceof McpSchema.TextResourceContents textContents) {
                        System.out.println("Dynamic resource content: " + textContents.text());
                    }
                }
            }
        } catch (McpError e) {
            System.err.println("MCP Error: " + e.getMessage());
        } finally {
            // Close the client
            client.close();
        }
    }
}
