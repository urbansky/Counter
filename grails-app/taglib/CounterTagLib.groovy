import static java.util.Calendar.*
import java.text.*

class CounterTagLib {

  /**
   * List of attributes:
   * 
   * countMonth
   * name
   * locale
   * gridColor
   * graphColor
   * graphWidth
   * id
   * graphId
   * width
   * height
   * onComplete
   */
  def counterMonthlyGraph = { attrs, body ->
    String id = attrs.id ?: ''
    
    // Erzeuge eine zufällige ID, damit auf einer HTML-Seite mehrere monthlyGraph-Elemente gerendert werden können 
    String graphId = attrs.graphId ?: Math.round(Math.random() * 1000000)
    
    String link = createLink(action: 'ajaxData', controller: 'counter')
    
    int countMonth = (attrs.countMonth as int) ?: 1
    Locale locale = attrs.locale ?: Locale.ENGLISH
    
    String gridColor = attrs.gridColor ?: "#333"
    String graphColor = attrs.graphColor ?: "#666"
    
    int width = attrs.width ? attrs.width as int : 800
    int height = attrs.height ? attrs.height as int : 250
    
    String labelY = attrs.caption ?: "{0} Hits"
    String labelY1 = attrs.caption1 ?: "1 Hit"
    
    String onComplete = attrs.onComplete ?: ""
    
    List comboData = []
    DateFormat formatter = new SimpleDateFormat("MMMM yyyy", locale)
    Date date = new Date()
    date[DAY_OF_MONTH] = 1
    countMonth.times {
      comboData << [name: formatter.format(date), id: date.format("MM-yyyy")]
      date[MONTH] -= 1
    }

    if (id) {
      out.println "<div id='${id}'>"
    } else {
      out.println "<div>"
    }
    out.println "<div>"
    out.println "  <span style='float:left' class='graphHeaderLeft'>"
    out.println "    <input id='month-prev-${graphId}' type='button' value='&lt;'/>"
    out.println select(id: "monthSelect-${graphId}", from: comboData, optionKey: "id", optionValue: "name", value: comboData[0].id)
    out.println "    <input id='month-next-${graphId}' type='button' value='&gt;'/>"
    out.println "  </span>"
    out.println "  <span style='float:right' class='graphHeaderRight'></span>"
    out.println "</div>"
    out.println "<div id='${graphId}'></div>"
    out.println """
      <script type="text/javascript">
        \$(function () {
          var idSplitStart = \$("#monthSelect-${graphId}").val().split("-");
          \$("#${graphId}").monthlyGraph({
            dataSource: "ajax",
            data: "${link}",
            ajaxParams: { year: idSplitStart[1], month: idSplitStart[0] - 1, name: "${attrs.name}" },
            labelX: function(value) { return value + ". " + \$("#monthSelect-${graphId} option:selected").text(); },
            gridColor: "${gridColor}",
            graphColor: "${graphColor}",
            labelY: function(value) { return "${labelY}".replace("{0}", value) },
            labelY1: function() { return "${labelY1}" },
            width: ${width},
            height: ${height},
            onComplete: function() { ${onComplete} }
          })
          var monthSelect = function() {
            var idSplit = \$("#monthSelect-${graphId}").val().split("-");
            with (\$("#${graphId}").data('monthlyGraph').settings.ajaxParams) { year= idSplit[1]; month= idSplit[0] - 1; };
            \$("#${graphId}").data('monthlyGraph').redraw();
          }
          \$("#monthSelect-${graphId}").change(monthSelect).keyup(monthSelect);
          \$("#month-prev-${graphId}").click(function() {
            \$("#monthSelect-${graphId}").val(\$("#monthSelect-${graphId} option:selected").next('option').val())
            monthSelect();
          });
          \$("#month-next-${graphId}").click(function() {
            \$("#monthSelect-${graphId}").val(\$("#monthSelect-${graphId} option:selected").prev('option').val())
            monthSelect();
          });
          
        });
      </script>
    """
    out.println "</div>"
  }
  
  /**
   * List of attributes:
   * 
   * omitJQuery
   */
  def counterRes = { attrs, body ->
    out.println """<script type="text/javascript" src="${resource(dir: 'js', file: 'raphael.js', plugin: 'counter')}"></script>"""
    out.println """<script type="text/javascript" src="${resource(dir: 'js', file: 'popup.js', plugin: 'counter')}"></script>"""
    if (!attrs.omitJQuery) out.println """<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-1.6.4.min.js', plugin: 'counter')}"></script>"""
    out.println """<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-monthlyGraph.js', plugin: 'counter')}"></script>"""
  }
}
