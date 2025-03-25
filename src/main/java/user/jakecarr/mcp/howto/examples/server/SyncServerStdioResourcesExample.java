package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example of implementing dynamic resources with templates in an MCP server.
 * 
 * This example demonstrates how to implement dynamic resources in an MCP server
 * using the resourceTemplates method in the server builder.
 */
public class SyncServerStdioResourcesExample {

    // Pattern for matching resource URIs
    private static final Pattern RESOURCE_PATTERN = Pattern.compile("example://([^/]+)/([^/]+)");

    public static void main(String[] args) throws Exception {
        // Create server info
        McpSchema.Implementation serverInfo = new McpSchema.Implementation("dynamic-resources-example", "1.0.0");
        
        // Create transport provider
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
        
        // Create a resource template
        McpSchema.ResourceTemplate template = new McpSchema.ResourceTemplate(
            "example://users/{userId}",
            "User Data",
            "Data for a specific user",
            "application/json",
            null
        );
        
        // Create a resource specification for the template
        Map<String, McpServerFeatures.SyncResourceSpecification> resourceSpecs = new HashMap<>();
        
        // Create a resource for the template pattern
        McpSchema.Resource resource = new McpSchema.Resource(
            "example://users/template",
            "User Data Template",
            "Template for user data",
            "application/json",
            null
        );
        
        // Create a resource specification with a handler
        McpServerFeatures.SyncResourceSpecification resourceSpec = 
            new McpServerFeatures.SyncResourceSpecification(
                resource,
                (exchange, request) -> {
                    String uri = request.uri();
                    
                    // Parse the URI to extract parameters
                    Matcher matcher = RESOURCE_PATTERN.matcher(uri);
                    
                    if (matcher.matches()) {
                        String category = matcher.group(1);
                        String id = matcher.group(2);
                        
                        // Generate dynamic content based on the category and ID
                        String userData = generateDynamicContent(category, id);
                        
                        List<McpSchema.ResourceContents> contents = new ArrayList<>();
                        contents.add(new McpSchema.TextResourceContents(
                            uri,
                            "application/json",
                            userData
                        ));
                        
                        return new McpSchema.ReadResourceResult(contents);
                    }
                    
                    throw new McpError(new McpSchema.JSONRPCResponse.JSONRPCError(
                        McpSchema.ErrorCodes.METHOD_NOT_FOUND,
                        "Resource not found: " + uri,
                        null
                    ));
                }
            );
        
        resourceSpecs.put("example://users/template", resourceSpec);
        
        // Create the server using the builder pattern
        McpSyncServer server = McpServer.sync(transportProvider)
            .serverInfo(serverInfo)
            .resources(resourceSpecs)
            .resourceTemplates(template)
            .build();
        
        System.out.println("Dynamic resources server running...");
    }
    
    /**
     * Generates dynamic content based on the category and ID.
     */
    private static String generateDynamicContent(String category, String id) {
        return String.format("Dynamic content for category '%s' and ID '%s'", category, id);
    }
}
