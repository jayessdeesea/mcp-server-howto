package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * Example of implementing dynamic resources in an MCP server.
 * 
 * This example demonstrates how to implement dynamic resources in an MCP server
 * using the resources method in the server builder.
 */
public class AsyncServerStdioResourcesExample {

    // Map of dynamic resources
    private static final Map<String, String> RESOURCES = new HashMap<>();
    
    static {
        RESOURCES.put("data://example/dynamic", "This is a dynamic resource");
        RESOURCES.put("data://example/another", "This is another dynamic resource");
    }

    public static void main(String[] args) throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("dynamic-resources-example", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create resource specifications
        Map<String, McpServerFeatures.AsyncResourceSpecification> resourceSpecs = new HashMap<>();
        
        // Add a resource specification for each dynamic resource
        for (Map.Entry<String, String> entry : RESOURCES.entrySet()) {
            String uri = entry.getKey();
            String content = entry.getValue();
            
            // Create a resource
            McpSchema.Resource resource = new McpSchema.Resource(
                uri,
                "Resource at " + uri,
                "A dynamic resource example",
                "application/json",
                null
            );
            
            // Create a resource specification with a handler
            McpServerFeatures.AsyncResourceSpecification resourceSpec = 
                new McpServerFeatures.AsyncResourceSpecification(
                    resource,
                    (exchange, request) -> {
                        // Return the resource content
                        List<McpSchema.ResourceContents> contents = new ArrayList<>();
                        contents.add(new McpSchema.TextResourceContents(
                            uri,
                            "application/json",
                            content
                        ));
                        
                        return Mono.just(new McpSchema.ReadResourceResult(contents));
                    }
                );
            
            resourceSpecs.put(uri, resourceSpec);
        }
        
        // Create the server using the builder pattern
        McpAsyncServer server = McpServer.async(transportProvider)
            .serverInfo(serverInfo)
            .resources(resourceSpecs)
            .build();
        
        System.out.println("Dynamic resources server running...");
    }
}
