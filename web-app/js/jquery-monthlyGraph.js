Raphael.fn.drawGrid = function (x, y, w, h, wv, hv, color) {
  //console.log(x + ", " + y + ", " + w + ", " + h + ", " + wv + ", " + hv)
  color = color || "#000";
  var path = ["M", Math.round(x) + .5, Math.round(y) + .5, "L", Math.round(x + w) + .5, Math.round(y) + .5, Math.round(x + w) + .5, Math.round(y + h) + .5, Math.round(x) + .5, Math.round(y + h) + .5, Math.round(x) + .5, Math.round(y) + .5],
      rowHeight = h / hv,
      columnWidth = w / wv;
  for (var i = 1; i < hv; i++) {
      path = path.concat(["M", Math.round(x) + .5, Math.round(y + i * rowHeight) + .5, "H", Math.round(x + w) + .5]);
  }
  for (i = 1; i < wv; i++) {
      path = path.concat(["M", Math.round(x + i * columnWidth) + .5, Math.round(y) + .5, "V", Math.round(y + h) + .5]);
  }
  return this.path(path.join(",")).attr({stroke: color});
};

// jQuery Plugin Boilerplate
// A boilerplate for jumpstarting jQuery plugins development
// version 1.1, May 14th, 2011
// by Stefan Gabos

// remember to change every instance of "pluginName" to the name of your plugin!

(function($) {

  // here we go!
  $.monthlyGraph = function(element, options) {

    // plugin's default options
    // this is private property and is accessible only from inside the plugin
    var defaults = {
      dataSource: "direct",
      data: {
        labels: ["Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"],
        data: [5, 18, 26, 23, 21, 4, 0]
      },
      ajaxParams: {},
      labelX: function(value) { return value + ". September 2011"},
      labelY: function(value) { return value + " Zugriffe"},
      labelY1: function() { return "1 Zugriff"},
      onComplete: function() { },
      gridColor: "#333",
      graphColor: "#666",
      graphWidth: 4,
      width: 800,
      height: 250
    }

    var plugin = this;

    plugin.settings = {}

    var $element = $(element);
    var element = element;

    var graphData = {};
    
    // the "constructor" method that gets called when the object is created
    plugin.init = function() {

      // the plugin's final properties are the merged default and user-provided
      // options (if any)
      plugin.settings = $.extend({}, defaults, options);

      graphData = {labels: [], data: []};
      
      grabData();
    }

    plugin.redraw = function() {
      grabData();
    }
    
    // private methods
    var grabData = function() {
      if (plugin.settings.dataSource == "table") {
        $("#" + plugin.settings.tableId + " tfoot th").each(function () { graphData.labels.push($(this).html()); });
        $("#" + plugin.settings.tableId + " tbody td").each(function () { graphData.data.push($(this).html()); });
        renderData();
      }
      if (plugin.settings.dataSource == "direct") {
        graphData = plugin.settings.data
        renderData();
      }
      if (plugin.settings.dataSource == "callback") {
        graphData = plugin.settings.data()
        renderData();
      }
      if (plugin.settings.dataSource == "ajax") {
        $.getJSON(plugin.settings.data, plugin.settings.ajaxParams, function(data) {
          graphData = data;
          renderData();
        })
      }
    }
    
    var renderData = function() {
      $element.empty();
      
      var labelX = plugin.settings.labelX;
      var labelY = plugin.settings.labelY;
      var labelY1 = plugin.settings.labelY1;

      var width = plugin.settings.width,
          height = plugin.settings.height,
          leftgutter = 0,
          bottomgutter = 20,
          topgutter = 20,
          //colorhue = .6 || Math.random(),
          //color = "hsl(" + [colorhue, .5, .5] + ")",
          color = plugin.settings.graphColor
          r = Raphael(element, width, height),
          txt = {font: '12px Helvetica, Arial', fill: "#fff"}, // Label im Popup
          txtLabel = {font: '12px Helvetica, Arial', fill: "#333"}, // Label
          txt1 = {font: '10px Helvetica, Arial', fill: "#fff"},
          txt2 = {font: '12px Helvetica, Arial', fill: "#000"},
          X = (width - leftgutter) / graphData.labels.length;
      var max = Math.max.apply(Math, graphData.data);
      if (max == 0) max = 10; // max = 0 geht nicht
      var Y = (height - bottomgutter - topgutter) / max;
      r.drawGrid(leftgutter + X * .5 + .5, topgutter + .5, width - leftgutter - X, height - topgutter - bottomgutter, 10, 10, plugin.settings.gridColor);
      var path = r.path().attr({stroke: color, "stroke-width": plugin.settings.graphWidth, "stroke-linejoin": "round"}),
          bgp = r.path().attr({stroke: "none", opacity: .3, fill: color}),
          label = r.set(),
          lx = 0, ly = 0,
          is_label_visible = false,
          leave_timer,
          blanket = r.set();
      label.push(r.text(60, 12, "24 hits").attr(txt));
      label.push(r.text(60, 27, "22 September 2008").attr(txt1)); //.attr({fill: color}));
      label.hide();
      var frame = r.popup(100, 100, label, "right").attr({fill: "#000", stroke: "#666", "stroke-width": 2, "fill-opacity": .7}).hide();

      var p, bgpp;
      for (var i = 0, ii = graphData.labels.length; i < ii; i++) {
          var y = Math.round(height - bottomgutter - Y * graphData.data[i]),
              x = Math.round(leftgutter + X * (i + .5)),
              t = r.text(x, height - 6, graphData.labels[i]).attr(txtLabel).toBack();
          if (!i) {
              p = ["M", x, y, "C", x, y];
              bgpp = ["M", leftgutter + X * .5, height - bottomgutter, "L", x, y, "C", x, y];
          }
          if (i && i < ii - 1) {
              var Y0 = Math.round(height - bottomgutter - Y * graphData.data[i - 1]),
                  X0 = Math.round(leftgutter + X * (i - .5)),
                  Y2 = Math.round(height - bottomgutter - Y * graphData.data[i + 1]),
                  X2 = Math.round(leftgutter + X * (i + 1.5));
              var a = getAnchors(X0, Y0, x, y, X2, Y2);
              p = p.concat([a.x1, a.y1, x, y, a.x2, a.y2]);
              bgpp = bgpp.concat([a.x1, a.y1, x, y, a.x2, a.y2]);
          }
          var dot = r.circle(x, y, 4).attr({fill: "#f0f0f0", stroke: color, "stroke-width": plugin.settings.graphWidth}); // Datenpunkt 
          blanket.push(r.rect(leftgutter + X * i, 0, X, height - bottomgutter).attr({stroke: "none", fill: "#fff", opacity: 0}));
          var rect = blanket[blanket.length - 1];
          (function (x, y, data, lbl, dot) {
              var timer, i = 0;
              rect.hover(function () {
                  clearTimeout(leave_timer);
                  var side = "right";
                  if (x + frame.getBBox().width > width) {
                      side = "left";
                  }
                  var ppp = r.popup(x, y, label, side, 1),
                      anim = Raphael.animation({
                          path: ppp.path,
                          transform: ["t", ppp.dx, ppp.dy]
                      }, 200 * is_label_visible);
                  lx = label[0].transform()[0][1] + ppp.dx;
                  ly = label[0].transform()[0][2] + ppp.dy;
                  frame.show().stop().animate(anim);
                  label[0].attr({text: data == 1 ? labelY1() : labelY(data)}).show().stop().animateWith(frame, anim, {transform: ["t", lx, ly]}, 200 * is_label_visible);
                  label[1].attr({text: labelX(lbl)}).show().stop().animateWith(frame, anim, {transform: ["t", lx, ly]}, 200 * is_label_visible);
                  dot.attr("r", 6);
                  is_label_visible = true;
              }, function () {
                  dot.attr("r", 4);
                  leave_timer = setTimeout(function () {
                      frame.hide();
                      label[0].hide();
                      label[1].hide();
                      is_label_visible = false;
                  }, 1);
              });
          })(x, y, graphData.data[i], graphData.labels[i], dot);
      }
      p = p.concat([x, y, x, y]);
      bgpp = bgpp.concat([x, y, x, y, "L", x, height - bottomgutter, "z"]);
      path.attr({path: p});
      bgp.attr({path: bgpp});
      frame.toFront();
      label[0].toFront();
      label[1].toFront();
      blanket.toFront();
      
      plugin.settings.onComplete()
    }
    
    var getAnchors = function(p1x, p1y, p2x, p2y, p3x, p3y) {
      var l1 = (p2x - p1x) / 2,
          l2 = (p3x - p2x) / 2,
          a = Math.atan((p2x - p1x) / Math.abs(p2y - p1y)),
          b = Math.atan((p3x - p2x) / Math.abs(p2y - p3y));
      a = p1y < p2y ? Math.PI - a : a;
      b = p3y < p2y ? Math.PI - b : b;
      var alpha = Math.PI / 2 - ((a + b) % (Math.PI * 2)) / 2,
          dx1 = l1 * Math.sin(alpha + a),
          dy1 = l1 * Math.cos(alpha + a),
          dx2 = l2 * Math.sin(alpha + b),
          dy2 = l2 * Math.cos(alpha + b);
      return {
          x1: p2x - dx1,
          y1: p2y + dy1,
          x2: p2x + dx2,
          y2: p2y + dy2
      };
    }
    
    // fire up the plugin!
    // call the "constructor" method
    plugin.init();
  }

  // add the plugin to the jQuery.fn object
  $.fn.monthlyGraph = function(options) {

    // iterate through the DOM elements we are attaching the plugin to
    return this.each(function() {

      // if plugin has not already been attached to the element
      if (undefined == $(this).data('monthlyGraph')) {

        // create a new instance of the plugin
        // pass the DOM element and the user-provided options as arguments
        var plugin = new $.monthlyGraph(this, options);

        // in the jQuery version of the element
        // store a reference to the plugin object
        // you can later access the plugin and its methods and properties like
        // element.data('pluginName').publicMethod(arg1, arg2, ... argn) or
        // element.data('pluginName').settings.propertyName
        $(this).data('monthlyGraph', plugin);
      }
    });
  }
})(jQuery); 