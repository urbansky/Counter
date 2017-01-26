class CounterGrailsPlugin {
    // the plugin version
    def version = "0.1.8"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Stefan Urbansky"
    def authorEmail = "urbansky@e-learning-service.de"
    def title = "Manage Counters"
    def description = '''\\
This plugin manage counters, e.g. you can create a simple access statistic for a website or it 
can be used as back-end for polling or voting. 
Every counter has a name and every increment is logged with date and time.
With the provided Taglib you can create line graphs for a monthly statistic.
'''

    // URL to the plugin's documentation
    def documentation = "http://www.webapp-blogger.de/grails-plugin-counter/"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
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
}
