package user.jakecarr.mcp.howto.examples.resources;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example of implementing static resources in an MCP server.
 * 
 * This example demonstrates how to implement static resources in an MCP server
 * using the resources method in the server builder.
 */
public class StaticResourcesExample {

    // Map of static resources
    private static final Map<String, String> RESOURCES = new HashMap<>();
    
    static {
        RESOURCES.put("data://example/static", "This is a static resource");
        RESOURCES.put("data://example/another", "This is another static resource");
    }

    public static void main(String[] args) throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("static-resources-example", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create resource specifications
        Map<String, McpServerFeatures.SyncResourceSpecification> resourceSpecs = new HashMap<>();
        
        // Add a resource specification for each static resource
        for (Map.Entry<String, String> entry : RESOURCES.entrySet()) {
            String uri = entry.getKey();
            String content = entry.getValue();
            
            // Create a resource
            McpSchema.Resource resource = new McpSchema.Resource(
                uri,
                "Resource at " + uri,
                "A static resource example",
                "application/json",
                null
            );
            
            // Create a resource specification with a handler
            McpServerFeatures.SyncResourceSpecification resourceSpec = 
                new McpServerFeatures.SyncResourceSpecification(
                    resource,
                    (exchange, request) -> {
                        // Return the resource content
                        List<McpSchema.ResourceContents> contents = new ArrayList<>();
                        contents.add(new McpSchema.TextResourceContents(
                            uri,
                            "application/json",
                            content
                        ));
                        
                        return new McpSchema.ReadResourceResult(contents);
                    }
                );
            
            resourceSpecs.put(uri, resourceSpec);
        }
        
        // Create the server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .resources(resourceSpecs)
            .build();
        
        System.out.println("Static resources server running...");
    }
}
