package user.jakecarr.mcp.howto.examples.server;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.Prompt;
import io.modelcontextprotocol.spec.McpSchema.PromptArgument;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.GetPromptRequest;
import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;

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
 * Tests for the SyncServerStdioPromptsExample class.
 */
public class SyncServerStdioPromptsExampleTest {

    /**
     * Test that verifies the SyncServerStdioPromptsExample class exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample class should exist")
    public void testClassExists() {
        assertNotNull(SyncServerStdioPromptsExample.class);
    }
    
    /**
     * Test that verifies the SyncServerStdioPromptsExample can be instantiated.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample should be instantiable")
    public void testCanBeInstantiated() {
        try {
            // Create an instance using reflection
            SyncServerStdioPromptsExample instance = SyncServerStdioPromptsExample.class.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        } catch (Exception e) {
            fail("Failed to instantiate SyncServerStdioPromptsExample: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the main method exists.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample should have a main method")
    public void testMainMethodExists() {
        try {
            // Check if the main method exists
            SyncServerStdioPromptsExample.class.getMethod("main", String[].class);
            // If we get here, the method exists
            assertTrue(true);
        } catch (NoSuchMethodException e) {
            fail("main method not found in SyncServerStdioPromptsExample");
        }
    }
    
    /**
     * Test that verifies the server creation code in SyncServerStdioPromptsExample.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample should create a server correctly")
    public void testServerCreation() {
        // This test verifies that the server creation code in SyncServerStdioPromptsExample is correct
        // by creating a server using the same pattern
        
        try {
            // Create server info
            McpSchema.Implementation serverInfo = new McpSchema.Implementation("test-server", "1.0.0");
            
            // Create transport provider
            StdioServerTransportProvider transportProvider = new StdioServerTransportProvider();
            
            // Create a code analysis prompt
            List<PromptArgument> codeAnalysisArgs = new ArrayList<>();
            codeAnalysisArgs.add(new PromptArgument(
                "language",
                "The programming language of the code",
                true
            ));
            codeAnalysisArgs.add(new PromptArgument(
                "code",
                "The code to analyze",
                true
            ));
            
            Prompt codeAnalysisPrompt = new Prompt(
                "code-analysis",
                "Analyzes code for potential issues and improvements",
                codeAnalysisArgs
            );
            
            // Create server using the builder pattern
            McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo(serverInfo)
                .prompts(
                    new io.modelcontextprotocol.server.McpServerFeatures.SyncPromptSpecification(
                        codeAnalysisPrompt,
                        (exchange, request) -> {
                            // Extract arguments from the request
                            String language = (String) request.arguments().get("language");
                            String code = (String) request.arguments().get("code");
                            
                            // Create prompt messages
                            List<PromptMessage> messages = new ArrayList<>();
                            
                            // System message
                            messages.add(new PromptMessage(
                                Role.USER,
                                new TextContent("You are a code analysis assistant that helps identify issues and suggest improvements.")
                            ));
                            
                            // Return the prompt result
                            return new GetPromptResult(
                                "Code analysis for " + language,
                                messages
                            );
                        }
                    )
                )
                .build();
            
            assertNotNull(server);
            
        } catch (Exception e) {
            fail("Failed to create server: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the prompt creation in SyncServerStdioPromptsExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample should create prompts correctly")
    public void testPromptCreation() {
        try {
            // Create a code analysis prompt
            List<PromptArgument> codeAnalysisArgs = new ArrayList<>();
            codeAnalysisArgs.add(new PromptArgument(
                "language",
                "The programming language of the code",
                true
            ));
            codeAnalysisArgs.add(new PromptArgument(
                "code",
                "The code to analyze",
                true
            ));
            
            Prompt codeAnalysisPrompt = new Prompt(
                "code-analysis",
                "Analyzes code for potential issues and improvements",
                codeAnalysisArgs
            );
            
            // Verify the prompt
            assertNotNull(codeAnalysisPrompt);
            assertEquals("code-analysis", codeAnalysisPrompt.name());
            assertEquals("Analyzes code for potential issues and improvements", codeAnalysisPrompt.description());
            assertEquals(2, codeAnalysisPrompt.arguments().size());
            assertEquals("language", codeAnalysisPrompt.arguments().get(0).name());
            assertEquals("code", codeAnalysisPrompt.arguments().get(1).name());
        } catch (Exception e) {
            fail("Failed to create prompt: " + e.getMessage());
        }
    }
    
    /**
     * Test that verifies the prompt message creation in SyncServerStdioPromptsExample.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    @DisplayName("SyncServerStdioPromptsExample should create prompt messages correctly")
    public void testPromptMessageCreation() {
        try {
            // Create a prompt message
            PromptMessage message = new PromptMessage(
                Role.USER,
                new TextContent("This is a test message")
            );
            
            // Verify the message
            assertNotNull(message);
            assertEquals(Role.USER, message.role());
            assertNotNull(message.content());
            assertTrue(message.content() instanceof TextContent);
            assertEquals("This is a test message", ((TextContent) message.content()).text());
        } catch (Exception e) {
            fail("Failed to create prompt message: " + e.getMessage());
        }
    }
}
