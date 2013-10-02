function drawGraph(data1, data2, data3, data4, data5)
{

	var myLine = new RGraph.Line('cvs',data1, data2)
		.Set('labels', ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'])
		.Set('gutter.left', 0)
		.Set('gutter.right', 0)
		.Set('gutter.bottom', 6)
		.Set('colors', ['#ff5555', 'black'])
		.Set('units.post', '%')
		.Set('linewidth', 1)
		.Set('hmargin', 15)
		.Set('text.color', '#333')
		.Set('text.font', 'Arial')
		.Set('background.grid.autofit', true)
		.Set('background.grid.autofit.numvlines', 11)
		.Set('shadow', false)
		.Set('shadow.color', 'rgba(20,20,20,0.3)')
		.Set('shadow.blur',  10)
		.Set('shadow.offsetx', 0)
		.Set('shadow.offsety', 0)
		.Set('background.grid.vlines', false)
		.Set('background.grid.border', true)
		.Set('noxaxis', true)
		.Set('title', '')
		.Set('axis.color', '#666')
		.Set('text.color', '#666')
		.Set('spline', true)

	/**
	* Use the Trace animation to show the chart
	*/
	if (ISOLD) {
		// IE7/8 don't support shadow blur, so set custom shadow properties
		myLine.Set('chart.shadow.offsetx', 3)
			.Set('chart.shadow.offsety', 3)
			.Set('chart.shadow.color', '#aaa')
			.Draw();
	} else {
		myLine.Set('chart.tooltips', [
									  
									 ]);

		
		RGraph.Effects.Line.jQuery.UnfoldFromCenterTrace(myLine, {'duration': 1000});
	}
	
	$("#duration").html(data3);
	$("#movements").html(data4);
	$("#maxEnergy").html(data5);
			
}


        
        
        
        
        
