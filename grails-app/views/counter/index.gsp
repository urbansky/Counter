<html lang="en">
  <head>
	  <title>Counter</title>
    <script src="${resource(dir: 'js', file: 'jquery-1.6.4.min.js')}"></script>
    <script src="${resource(dir: 'js', file: 'raphael.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery-monthlyGraph.js')}"></script>
    <script src="${resource(dir: 'js', file: 'popup.js')}"></script>

    <link href='http://fonts.googleapis.com/css?family=Maven+Pro:500' rel='stylesheet' type='text/css'>
	  <link href='http://fonts.googleapis.com/css?family=Yanone+Kaffeesatz:700' rel='stylesheet' type='text/css'>
	  <style type="text/css">
			body { 
				/*background: url(${resource(dir: 'images', file: 'background.jpg')}) repeat;  */
				color: #666; font-family: "Maven Pro", "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif; 
			}
			a { text-decoration: none; }
			a:hover { text-decoration: underline; }
			h1 {
			  margin: 20px 0 0 0;
			  font-family: 'Yanone Kaffeesatz', "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif; 
			  text-align: center;
			  font-size: 180px;
			  color: #fd8d46;
			  text-shadow: 5px 3px 0px #888; 
			  text-decoration: none;
			}
			h2 {
			  font-family: 'Yanone Kaffeesatz', "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif; 
			  text-shadow: 1px 1px 0px #fff;
			  font-size: 42px;
			  text-align: left;
			  color: #666;
			}
			h3 {
			  margin: 10px 0;
			  font-family: 'Yanone Kaffeesatz', "Helvetica Neue", Helvetica, "Arial Unicode MS", Arial, sans-serif; 
			  text-align: left;
			  text-shadow: 1px 1px 0px #fff;
			  font-size: 36px;
			  color: #666;
			  border-bottom: 2px solid #666;
			}
			#logo {
				width: 980px;
				margin: 0 auto 80px auto;
				
			}
			#container {
				top: 0; left: 0; width: 100%;
				position: absolute;
			}
			#logo h1 { margin-bottom: 0; }
			#logo h2 { margin-top: -30px; text-align: center;}
			article { width: 980px; margin: 20px auto; }
			#counterList { margin-bottom: 20px; }
			.select { display: inline-block; border: 1px solid #666; padding: 2px 6px; margin-right: 6px; border-radius: 4px; cursor: pointer; }
			.select:hover { background-color: #eee; }
			.select.selected { background-color: #666; color: #f8f8f8; }
			.badge { 
				display: inline-block; padding: 1px 4px; margin: 0 0 14px -19px; border-radius: 7px; 
				background-color: #d92e2e; font-size: 12px; color: white; vertical-align: bottom;
				background-color: #fd8d46;
				background-color: #cb7840;
			}
			.counterDate { display: inline-block; width: 160px; }
			.counterTime { display: inline-block; width: 70px; }
			#counterHeader span { font-weight: bold; font-size: larger; }
			
			.button { 
				display: inline-block; border: 1px solid #666; padding: 2px 6px; margin-right: 6px; border-radius: 4px; cursor: pointer; 
				color: #333; background-color: #f8f8f8;
				text-decoration: none; 
			}
			.button:hover { background-color: #eee; text-decoration: none; }
			
	  </style>
    <script type="text/javascript">
		  $(function () {
		    $("#counterList span").click(function() {
		      $("#counterList span").removeClass("selected");
		      $(this).addClass("selected");

					$("#counter").data('monthlyGraph').settings.ajaxParams.name = $(this).text();
					$("#counter").data('monthlyGraph').redraw();
		    });
		    $("#counterList span").first().addClass("selected");
		    $("#manualIncrementSubmit").click(function() {
		      var counterURL = "${createLink(action: 'increment', controller: 'counter')}"
			    $.get(counterURL, {name: $("#manualIncrement").val()}, function() { location.reload(); });
			    return false;
		    })
		    $("#manualIncrementRandomSubmit").click(function() {
		      var counterURL = "${createLink(action: 'incrementRandom', controller: 'counter')}"
			    $.get(counterURL, {name: $("#manualIncrement").val()}, function() { location.reload(); });
			    return false;
		    })
		    $("#deleteCounterSubmit").click(function() {
		      var deleteURL = "${createLink(action: 'delete', controller: 'counter')}"
			    $.get(deleteURL, {name: $("#manualIncrement").val()}, function() { location.reload(); });
			    return false;
		    })
			});

			function graphReady() {
			  $("#graph .graphHeaderRight").text("Example right component");
			}
	  </script>
  </head>

  <body>
  	<div id="container">
	  	<header id="logo">
	    	<h1>Counter</h1>
	    	<h2>Grails-Plugin: Manage Counters</h2>
	    </header>
	    
	    <article>
				<h3>Description</h3>
		    <p>
			    This plugin manage counters, e.g. you can create a simple access statistic for a website or it can be used as back-end for polling or voting. 
			    Every counter has a name and every increment is logged with date and time.   
		    </p>
	    </article>
	    <article>
				<h3>All counters with monthly graph</h3>
				<p>Total counts: ${totalCount}</p>
				<g:if test="${counterList.size() == 0}">There are no counters.</g:if>
				<g:else>
					<div id="counterList">
						Select counter:
						<g:each in="${counterList}" var="counter">
							<span class="select">${counter[0]}</span>
							<span class="badge">${counter[1]}</span>
						</g:each>
					</div>
					<g:counterMonthlyGraph countMonth="6" name="${counterList[0][0]}" gridColor="#aaa" graphColor="#666" graphId="counter" id="graph" width="980" onComplete="graphReady();"/>
				</g:else>
			</article>
			
	    <article>
				<h3>Create/Increment or Delete a counter</h3>
				<div>
					<label for="manualIncrement">Counter: </label>
					<g:textField name="manualIncrement" />
					<a href="#" id="manualIncrementSubmit" class="button">+1 (now)</a>
					<a href="#" id="manualIncrementRandomSubmit" class="button">+60 (random date)</a>
					<a href="#" id="deleteCounterSubmit" class="button">Delete counter</a>
				</div>
			</article>
			
	    <article>
				<h3>Last 100 Increments</h3>
				<div id="last100">
						<div id="counterHeader">
							<span class="counterDate">Date</span>
							<span class="counterTime">Time</span>
							<span class="counterName">Counter</span>
						</div>
					<g:each in="${last100}" var="counter">
						<div>
							<span class="counterDate"><g:formatDate date="${counter.dateCreated}" type="date" style="LONG" timeStyle="SHORT"/></span>
							<span class="counterTime"><g:formatDate date="${counter.dateCreated}" type="time" style="LONG" timeStyle="SHORT"/></span>
							<span class="counterName">${counter.name}</span>
						</div>
					</g:each>
				</div>
			</article>
			
		</div>
  </body>
</html>