
var vis;
var canvas = false;     
    
    

function init(val) {
    
//    canvas = new $jit.ST({
//        injectInto: 'infovis'
//        }).canvas;
//    
    var fn = window[val];
    
    fn();
    
}



function extendsTree() {
    
    var json = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "./data/ExtendTree.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
    })();
    //var json = require('./data/ExtendTree.json');//= { "id": "java.lang.Object", "name": "Object", "data": {"package": "java.lang"}, "children": [{ "id": "com.smanzana.Exploratory2.Representations.Method", "name": "Method", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.DirectedGraphNode", "name": "DirectedGraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.DirectedGraphNode", "name": "DirectedGraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]} , { "id": "com.smanzana.Exploratory2.Representations.Cclass", "name": "Cclass", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Driver", "name": "Driver", "data": {"package": "com.smanzana.Exploratory2"}, "children": []} , { "id": "com.smanzana.Exploratory2.FileParsing.FileParser", "name": "FileParser", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Tree.Tree", "name": "Tree", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": []} , { "id": "com.smanzana.Exploratory2.Representations.Import", "name": "Import", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Util.Pair<a,b>", "name": "Pair<a,b>", "data": {"package": "com.smanzana.Exploratory2.Util"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.UndirectedWeightedEdge", "name": "UndirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.Tree.wowInterface", "name": "wowInterface", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": [{ "id": "com.smanzana.Exploratory2.Tree.wowInterface", "name": "wowInterface", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": [{ "id": "com.smanzana.Exploratory2.Tree.Wow", "name": "Wow", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": []}]}]} , { "id": "com.smanzana.Exploratory2.FileParsing.ClassDeclaration", "name": "ClassDeclaration", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.GraphNode", "name": "GraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.FileParsing.JSON", "name": "JSON", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.Graph", "name": "Graph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.UndirectedGraph", "name": "UndirectedGraph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.DirectedGraph", "name": "DirectedGraph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]} , { "id": "com.smanzana.Exploratory2.Graph.DirectedWeightedEdge", "name": "DirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.DirectedWeightedEdge", "name": "DirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]}]};
 //   require(['./data/ExtendTree.json'], function(json) {
 //       //nothing?
 //   });
//    $.getJSON("./data/ExtendTree.json", function(file) {
//        json = file;
//    });
    
    vis = new $jit.ST({
    
        useCanvas: canvas,
        injectInto: 'infovis',
        type: '2D',
        width: 700,
        height: 700,
        offsetX: 250,
        
        Navigation: {
          enable:true,
          panning:true
        },
                
        Node: {
            width: 200,
            autoWidth: true,
            autoHeight: true,
            overridable: true,
            align: 'left'
        },
        
        
        Edge: {
          overridable: true,
          color: '#00AA11',
          lineWidth: 1.8
        },
        
        
        levelDistance: 130,
        
//        onBeforePlotNode: function(node) {
//            node.width = (node.name.length * 20) + 'px';
//            node.setCanvasStyle('width', (node.name.length * 10) + 'px');
//        },
        
        onCreateLabel: function(label, node){
            label.id = node.id;            
            label.innerHTML = ' ' + node.name;
            label.onclick = function(){
            	if(normal.checked) {
            	  st.onClick(node.id);
            	} else {
                st.setRoot(node.id, 'animate');
            	}
            };
            //set label styles
            var style = label.style;
            style.width = (node.name.length * 10);
            style.height = 17 + 'px';            
            style.cursor = 'pointer';
            style.color = '#333';
            style.backgroundColor = '#EEFFEE';
            style.fontSize = '10pt';
            style.textAlign= 'left';
            style.paddingTop = '3px';
            style.paddingLeft = '2px';
            //style.paddingRight = '5px';
        }
    });
    
    if (canvas == false) {
        canvas = vis.canvas;
    }
    
    
    //load json data
    vis.loadJSON(json);
    //compute node positions and layout
    vis.compute();
    //optional: make a translation of the tree
    //st.geom.translate(new $jit.Complex(-200, 0), "current");
    //emulate a click on the root node.
    vis.onClick(vis.root);
}

function ImplementsGraph() {
    
    var json = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "./data/ImplementGraph.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
    })();
    
    var vis = new $jit.ForceDirected({
        useCanvas: canvas,
        injectInto: 'infovis',
        type: '2D',
        width: 700,
        height: 700,
        Navigation: {
          enable: true,
          //Enable panning events only if we're dragging the empty
          //canvas (and not a node).
          panning: 'avoid nodes',
          zooming: 10 //zoom speed. higher is more sensible
        },
        // Change node and edge styles such as
        // color and width.
        // These properties are also set per node
        // with dollar prefixed data-properties in the
        // JSON structure.
        Node: {
          overridable: true,
          color: '#00FF00',
          size: 50
        },
        Edge: {
          overridable: true,
          color: '#00AA11',
          lineWidth: 1.8
        },
        //Native canvas text styling
        Label: {
          size: 10,
          style: 'bold'
        },
        //Add Tips
        Tips: {
          enable: true,
          onShow: function(tip, node) {
            //count connections
            var count = 0;
            node.eachAdjacency(function() { count++; });
            //display node info in tooltip
            tip.innerHTML = "<div class=\"tip-title\">" + node.name + "</div>"
              + "<div class=\"tip-text\"><b>connections:</b> " + count + "</div>";
          }
        },
        // Add node events
        Events: {
          enable: true,
          type: 'Native',
          //Change cursor style when hovering a node
          onMouseEnter: function() {
            vis.canvas.getElement().style.cursor = 'move';
          },
          onMouseLeave: function() {
            vis.canvas.getElement().style.cursor = '';
          },
          //Update node positions when dragged
          onDragMove: function(node, eventInfo, e) {
              var pos = eventInfo.getPos();
              node.pos.setc(pos.x, pos.y);
              vis.plot();
          },
          //Implement the same handler for touchscreens
          onTouchMove: function(node, eventInfo, e) {
            $jit.util.event.stop(e); //stop default touchmove event
            this.onDragMove(node, eventInfo, e);
          },
          //Add also a click handler to nodes
          onClick: function(node) {
            if(!node) return;
            // Build the right column relations list.
            // This is done by traversing the clicked node connections.
            var html = "<h4>" + node.name + "</h4><b> connections:</b><ul><li>",
                list = [];
            node.eachAdjacency(function(adj){
              list.push(adj.nodeTo.name);
            });
            //append connections information
            //$jit.id('inner-details').innerHTML = html + list.join("</li><li>") + "</li></ul>";
          }
        },
        //Number of iterations for the FD algorithm
        iterations: 200,
        //Edge length
        levelDistance: 100,
        // Add text to the labels. This method is only triggered
        // on label creation and only for DOM labels (not native canvas ones).
        onCreateLabel: function(domElement, node){
          domElement.innerHTML = node.name;
          var style = domElement.style;
          style.fontSize = "0.8em";
            style.color = '#333';
            style.backgroundColor = '#EEFFEE';
        },
        // Change node styles when DOM labels are placed
        // or moved.
        onPlaceLabel: function(domElement, node){
          var style = domElement.style;
          var left = parseInt(style.left);
          var top = parseInt(style.top);
          var w = domElement.offsetWidth;
          style.left = ((left - w / 2)+10) + 'px';
          style.top = (top + 10) + 'px';
          style.display = '';
        }
        
      });
      
      
        if (canvas == false) {
            canvas = vis.canvas;
        }
        
      // load JSON data.
      vis.loadJSON(json);
      // compute positions incrementally and animate.
      vis.computeIncremental({
        iter: 40,
        property: 'end',
        onStep: function(perc){
          //Log.write(perc + '% loaded...');
        },
        onComplete: function(){
          //Log.write('done');
          vis.animate({
            modes: ['linear'],
            transition: $jit.Trans.Elastic.easeOut,
            duration: 2500
          });
        }
      });
}





/*
var labelType, useGradients, nativeTextSupport, animate;

(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport 
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
  nativeTextSupport = labelType == 'Native';
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();

//var Log = {
//  elem: false,
//  write: function(text){
//    if (!this.elem) 
//      this.elem = document.getElementById('log');
//    this.elem.innerHTML = text;
//    this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
//  }
//};


function init(){

    //init data
    var json = { "id": "java.lang.Object", "name": "Object", "data": {"package": "java.lang"}, "children": [{ "id": "com.smanzana.Exploratory2.Representations.Method", "name": "Method", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.DirectedGraphNode", "name": "DirectedGraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.DirectedGraphNode", "name": "DirectedGraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]} , { "id": "com.smanzana.Exploratory2.Representations.Cclass", "name": "Cclass", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Driver", "name": "Driver", "data": {"package": "com.smanzana.Exploratory2"}, "children": []} , { "id": "com.smanzana.Exploratory2.FileParsing.FileParser", "name": "FileParser", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Tree.Tree", "name": "Tree", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": []} , { "id": "com.smanzana.Exploratory2.Representations.Import", "name": "Import", "data": {"package": "com.smanzana.Exploratory2.Representations"}, "children": []} , { "id": "com.smanzana.Exploratory2.Util.Pair<a,b>", "name": "Pair<a,b>", "data": {"package": "com.smanzana.Exploratory2.Util"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.UndirectedWeightedEdge", "name": "UndirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.Tree.wowInterface", "name": "wowInterface", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": [{ "id": "com.smanzana.Exploratory2.Tree.wowInterface", "name": "wowInterface", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": [{ "id": "com.smanzana.Exploratory2.Tree.Wow", "name": "Wow", "data": {"package": "com.smanzana.Exploratory2.Tree"}, "children": []}]}]} , { "id": "com.smanzana.Exploratory2.FileParsing.ClassDeclaration", "name": "ClassDeclaration", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.GraphNode", "name": "GraphNode", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.FileParsing.JSON", "name": "JSON", "data": {"package": "com.smanzana.Exploratory2.FileParsing"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.Graph", "name": "Graph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.UndirectedGraph", "name": "UndirectedGraph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []} , { "id": "com.smanzana.Exploratory2.Graph.DirectedGraph", "name": "DirectedGraph", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]} , { "id": "com.smanzana.Exploratory2.Graph.DirectedWeightedEdge", "name": "DirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": [{ "id": "com.smanzana.Exploratory2.Graph.DirectedWeightedEdge", "name": "DirectedWeightedEdge", "data": {"package": "com.smanzana.Exploratory2.Graph"}, "children": []}]}]};
    //end
    //init Spacetree
    //Create a new ST instance
    var st = new $jit.ST({
        //id of viz container element
        injectInto: 'infovis',
        //set duration for the animation
        duration: 800,
        //set animation transition type
        transition: $jit.Trans.Quart.easeInOut,
        //set distance between node and its children
        levelDistance: 50,
        //enable panning
        Navigation: {
          enable:true,
          panning:true
        },
        //set node and edge styles
        //set overridable=true for styling individual
        //nodes or edges
        Node: {
            height: 20,
            width: 60,
            type: 'rectangle',
            color: '#aaa',
            overridable: true
        },
        
        Edge: {
            type: 'bezier',
            overridable: true
        },
        
        onBeforeCompute: function(node){
            //Log.write("loading " + node.name);
        },
        
        onAfterCompute: function(){
            //Log.write("done");
        },
        
        //This method is called on DOM label creation.
        //Use this method to add event handlers and styles to
        //your node.
        onCreateLabel: function(label, node){
            label.id = node.id;            
            label.innerHTML = node.name;
            label.onclick = function(){
            	if(normal.checked) {
            	  st.onClick(node.id);
            	} else {
                st.setRoot(node.id, 'animate');
            	}
            };
            //set label styles
            var style = label.style;
            style.width = 60 + 'px';
            style.height = 17 + 'px';            
            style.cursor = 'pointer';
            style.color = '#333';
            style.fontSize = '0.8em';
            style.textAlign= 'center';
            style.paddingTop = '3px';
        },
        
        //This method is called right before plotting
        //a node. It's useful for changing an individual node
        //style properties before plotting it.
        //The data properties prefixed with a dollar
        //sign will override the global node style properties.
        onBeforePlotNode: function(node){
            //add some color to the nodes in the path between the
            //root node and the selected node.
            if (node.selected) {
                node.data.$color = "#ff7";
            }
            else {
                delete node.data.$color;
                //if the node belongs to the last plotted level
                if(!node.anySubnode("exist")) {
                    //count children number
                    var count = 0;
                    node.eachSubnode(function(n) { count++; });
                    //assign a node color based on
                    //how many children it has
                    node.data.$color = ['#aaa', '#baa', '#caa', '#daa', '#eaa', '#faa'][count];                    
                }
            }
        },
        
        //This method is called right before plotting
        //an edge. It's useful for changing an individual edge
        //style properties before plotting it.
        //Edge data proprties prefixed with a dollar sign will
        //override the Edge global style properties.
        onBeforePlotLine: function(adj){
            if (adj.nodeFrom.selected && adj.nodeTo.selected) {
                adj.data.$color = "#eed";
                adj.data.$lineWidth = 3;
            }
            else {
                delete adj.data.$color;
                delete adj.data.$lineWidth;
            }
        }
    });
    //load json data
    st.loadJSON(json);
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new $jit.Complex(-200, 0), "current");
    //emulate a click on the root node.
    st.onClick(st.root);
    //end
    //Add event handlers to switch spacetree orientation.
    //var top = $jit.id('r-top'), 
    //    left = $jit.id('r-left'), 
    //    bottom = $jit.id('r-bottom'), 
    //    right = $jit.id('r-right'),
    //    normal = $jit.id('s-normal');
        
    
//    function changeHandler() {
//        if(this.checked) {
//            top.disabled = bottom.disabled = right.disabled = left.disabled = true;
//            st.switchPosition(this.value, "animate", {
//                onComplete: function(){
//                    top.disabled = bottom.disabled = right.disabled = left.disabled = false;
//                }
//            });
//        }
//    };
    
    //top.onchange = left.onchange = bottom.onchange = right.onchange = changeHandler;
    //end

}
*/
