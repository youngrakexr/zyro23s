import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;

import grails.plugin.springwebsocket.GrailsSimpAnnotationMethodMessageHandler;
import grails.plugin.springwebsocket.WebSocketConfig;

class SpringWebsocketGrailsPlugin {
	
	def version = "0.1.BUILD-SNAPSHOT"
	def grailsVersion = "2.4 > *"
	def pluginExcludes = ["grails-app/views/error.gsp"]

	def title = "Spring Websocket Plugin" // Headline display name of the plugin
	def author = "zyro"
	def authorEmail = ""
	def description = "Spring Websocket Plugin"

	def documentation = "http://grails.org/plugin/spring-websocket"

	// Extra (optional) plugin metadata

	// License: one of 'APACHE', 'GPL2', 'GPL3'
	//    def license = "APACHE"

	// Details of company behind the plugin (if there is one)
	//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

	// Any additional developers beyond the author specified above.
	//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

	// Location of the plugin's issue tracker.
	//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

	// Online location of the plugin's browseable source code.
	//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

	def doWithWebDescriptor = { xml ->
		def config = application.config.grails?.plugin?.springwebsocket
		def additionalMappings = config?.dispatcherServlet?.additionalMappings ?: ["/*"]
		additionalMappings.each { urlPattern ->
			xml."servlet-mapping"[-1] + {
				"servlet-mapping" {
					"servlet-name" "grails"
					"url-pattern" urlPattern
				}
			}
		}
	}

	def doWithSpring = {
		def config = application.config.grails?.plugin?.springwebsocket
		
		httpRequestHandlerAdapter HttpRequestHandlerAdapter
		
		if (!config.useCustomConfig) {
			webSocketConfig WebSocketConfig
			
			grailsSimpAnnotationMethodMessageHandler(
				GrailsSimpAnnotationMethodMessageHandler,
				ref("brokerMessagingTemplate"),
				ref("webSocketResponseChannel")
			) {
				destinationPrefixes = config?.messageBroker?.applicationDestinationPrefixes ?: WebSocketConfig.DEFAULT_APPLICATION_DESTINATION_PREFIXES
			}
		}
	}

	def doWithDynamicMethods = { ctx ->
		// TODO Implement registering dynamic methods to classes (optional)
	}

	def doWithApplicationContext = { ctx ->
		// TODO Implement post initialization spring config (optional)
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}

	def onShutdown = { event ->
		// TODO Implement code that is executed when the application shuts down (optional)
	}
}
