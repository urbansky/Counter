import static java.util.Calendar.*
import grails.converters.*

class CounterController {
  
  def counterService
  
  def index = {
    def cc = counterService.counterCounts
    [
      counterList: counterService.counterCounts,
      totalCount: cc.sum { it[1] },
      last100: Counter.list(max: 100, sort: "dateCreated", order: "desc")
    ]
  }

  def increment = {
    counterService.increment(params)
    renderNothing()
  }

  def incrementRandom = {
    60.times {
      params.dateCreated = (new Date()) - (Math.round(60 * Math.random()) as int)
      counterService.increment(params)
    }
    renderNothing()
  }
  
  private void renderNothing() {
    response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
    response.addDateHeader("Expires", 0);
    response.setDateHeader("max-age", 0);
    response.setIntHeader("Expires", -1); //prevents caching at the proxy server
    response.setContentType("text/javascript")
    render ""
  }
  
  /**
   * Params:
   * year = "2011"
   * month = "0" (January)
   */
  def ajaxData = {
    Date m = new Date()
    m.clearTime()
    m[DAY_OF_MONTH] = 1
    m[YEAR] = params.year as int
    m[MONTH] = params.month as int
    params.month = m

    List statData = counterService.getCounterData(params)

    render(contentType: "text/json") {
      labels = 1..statData.size()
      data = statData
    }
  }
  
  def delete = {
    counterService.deleteCounter(params)
    renderNothing()
  }
}
