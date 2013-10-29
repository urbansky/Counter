modules = {

  jquery {
    resource url: '/js/jquery-1.6.4.min.js'
  }

  raphael {
    dependsOn 'jquery'
    resource url: '/js/raphael.js'
  }

  modernizr {
    resource url: '/js/modernizr-2.0.min.js'
  }

  monthly {
    dependsOn 'raphael'
    resource url: '/js/jquery-monthlyGraph.js'
  }

  popup {
    dependsOn 'raphael'
    resource url: '/js/popup.js'
  }

  counter {
    dependsOn 'raphael, monthly, popup'
  }

}