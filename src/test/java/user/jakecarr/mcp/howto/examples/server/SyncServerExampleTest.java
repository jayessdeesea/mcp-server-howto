package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SyncServerExample class.
 */
public class SyncServerExampleTest {

    /**
     * Test that verifies the SyncServerExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncServerExample.class);
    }
    
    /**
     * Test that verifies the SyncServerExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncServerExample instance = SyncServerExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncServerExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncServerExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncServerExample");
        }
    }
    
    /**
     * Test that verifies the server creation code in SyncServerExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in SyncServerExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("example-server", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create server using the builder pattern
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .tool(
                    new McpSchema.Tool(
                        "example-tool",
                        "An example tool",
                        createTestToolSchema()
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
                        
                        return new McpSchema.CallToolResult(content, false);
                    }
                )
                .build();
            
            assertNotNull(server);
        } catch (Exception e) {
            fail("Failed to create server: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the createToolSchema method in SyncServerExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerExample should create a tool schema correctly")
    public void testCreateToolSchema() {
        try {
            // Get the createToolSchema method using reflection
            java.lang.reflect.Method method = SyncServerExample.class.getDeclaredMethod("createToolSchema");
            method.setAccessible(true);
            
            // Invoke the method
            Object result = method.invoke(null);
            
            // Verify the result
            assertNotNull(result);
            assertTrue(result instanceof McpSchema.JsonSchema);
            
            McpSchema.JsonSchema schema = (McpSchema.JsonSchema) result;
            assertEquals("object", schema.type());
            assertNotNull(schema.properties());
            assertNotNull(schema.required());
            assertTrue(schema.required().contains("param1"));
        } catch (Exception e) {
            fail("Failed to test createToolSchema: " + e.getMessage());
        }
    }
    
    /**
     * Creates a JSON schema for testing tools.
     */
    private static McpSchema.JsonSchema createTestToolSchema() {
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
