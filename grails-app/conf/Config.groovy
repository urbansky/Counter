// configuration for plugin testing - will not be included in the plugin zip
 
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

   info 'grails.app.controller',
           'grails.app.service',
           'grails.app.tagLib',
           'grails.app.bootstrap'
           
    warn   'org.mortbay.log'
}


grails.doc.images = new File("src/docs/images")
grails.doc.authors = "Stefan Urbansky"
grails.doc.license = "Apache License, Version 2.0"
grails.doc.copyright = "Apache License, Version 2.0"
grails.doc.footer = ""
