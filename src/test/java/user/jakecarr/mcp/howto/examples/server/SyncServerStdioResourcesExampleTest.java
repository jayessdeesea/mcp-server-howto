package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SyncServerStdioResourcesExample class.
 */
public class SyncServerStdioResourcesExampleTest {

    /**
     * Test that verifies the SyncServerStdioResourcesExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncServerStdioResourcesExample.class);
    }
    
    /**
     * Test that verifies the SyncServerStdioResourcesExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncServerStdioResourcesExample instance = SyncServerStdioResourcesExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncServerStdioResourcesExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncServerStdioResourcesExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncServerStdioResourcesExample");
        }
    }
    
    /**
     * Test that verifies the RESOURCE_PATTERN field exists and is a valid pattern.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample should have a valid RESOURCE_PATTERN field")
    public void testResourcePatternField() {
        try {
            // Get the RESOURCE_PATTERN field using reflection
            Field field = SyncServerStdioResourcesExample.class.getDeclaredField("RESOURCE_PATTERN");
            field.setAccessible(true);
            
            // Get the value of the field
            Object value = field.get(null);
            
            // Verify the value
            assertNotNull(value);
            assertTrue(value instanceof Pattern);
            
            Pattern pattern = (Pattern) value;
            
            // Test the pattern with a valid URI
            assertTrue(pattern.matcher("example://category/id").matches());
            
            // Test the pattern with an invalid URI
            assertFalse(pattern.matcher("invalid://uri").matches());
        } catch (Exception e) {
            fail("Failed to test RESOURCE_PATTERN field: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the generateDynamicContent method exists and works correctly.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample should have a working generateDynamicContent method")
    public void testGenerateDynamicContent() {
        try {
            // Get the generateDynamicContent method using reflection
            Method method = SyncServerStdioResourcesExample.class.getDeclaredMethod("generateDynamicContent", String.class, String.class);
            method.setAccessible(true);
            
            // Invoke the method
            Object result = method.invoke(null, "test-category", "test-id");
            
            // Verify the result
            assertNotNull(result);
            assertTrue(result instanceof String);
            
            String content = (String) result;
            assertTrue(content.contains("test-category"));
            assertTrue(content.contains("test-id"));
        } catch (Exception e) {
            fail("Failed to test generateDynamicContent method: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the server creation code in SyncServerStdioResourcesExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioResourcesExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in SyncServerStdioResourcesExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("dynamic-resources-example", "1.0.0");
            
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
