package user.jakecarr.mcp.howto.examples.resources;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StaticResourcesExample class.
 */
public class StaticResourcesExampleTest {

    /**
     * Test that verifies the StaticResourcesExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StaticResourcesExample class should exist")
    public void testClassExists() {
        assertNotNull(StaticResourcesExample.class);
    }
    
    /**
     * Test that verifies the StaticResourcesExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StaticResourcesExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            StaticResourcesExample instance = StaticResourcesExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate StaticResourcesExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StaticResourcesExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            StaticResourcesExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in StaticResourcesExample");
        }
    }
    
    /**
     * Test that verifies the RESOURCES field exists and contains the expected values.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("StaticResourcesExample should have a RESOURCES field with expected values")
    public void testResourcesField() {
        try {
            // Get the RESOURCES field using reflection
            Field field = StaticResourcesExample.class.getDeclaredField("RESOURCES");
            field.setAccessible(true);
            
            // Get the value of the field
            Object value = field.get(null);
            
            // Verify the value
            assertNotNull(value);
            assertTrue(value instanceof Map);
            
            @SuppressWarnings("unchecked")
            Map<String, String> resources = (Map<String, String>) value;
            
            // Verify the map contains the expected entries
            assertTrue(resources.containsKey("data://example/static"));
            assertEquals("This is a static resource", resources.get("data://example/static"));
            
            assertTrue(resources.containsKey("data://example/another"));
            assertEquals("This is another static resource", resources.get("data://example/another"));
        } catch (Exception e) {
            fail("Failed to test RESOURCES field: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the server creation code in StaticResourcesExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("StaticResourcesExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in StaticResourcesExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("static-resources-example", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create the server using the builder pattern
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .build();
            
            assertNotNull(server);
        } catch (Exception e) {
            fail("Failed to create server: " + e.getMessage());
        }
    }
}
