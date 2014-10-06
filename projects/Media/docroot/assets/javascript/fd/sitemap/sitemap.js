//depends on http://d3js.org/d3.v3.min.js

//document.getElementById("spinner").style.display="none";
//document.getElementById("log").style.display="none";

var buff = [], csvbuff = [];

data.countAll = data.children.reduce(function (p, c) { return p + c.countAll; }, 0);

function displayNode (node, buff) {
  buff.push('<li id="'+node.id+'" data-products="'+node.countAll+'" data-products-avail="'+node.countAvailable+'" data-products-tempunavail="'+node.countTempUnavailable+'" data-products-disc="'+node.countDiscontinued+'"><a href="/browse.jsp?id='+node.id+'">'+node.name+'<span class="contentId">'+node.id+'</span></a>');

  if (node.children && node.children.length) {
    buff.push('<span class="handle"></span><ul>');
    node.children.forEach(function (childnode) {
      buff = displayNode(childnode, buff);
    });
    buff.push('</ul>');
  }

  buff.push('</li>');
  return buff;
}

function displayCSVNode (node, csvbuff, parents) {
  parents = parents || [];
  var family = parents.concat([node]),
      line = [];


  line = [node.countAll, node.countAvailable, node.countTempUnavailable, node.countDiscontinued];

  parents.forEach(function (parent) {
    line.push(parent.name);
    line.push(parent.id);
  });

  line.push(node.name);
  line.push(node.id);

  csvbuff.push(line.map(function (item) {
    item = ""+item;
    return '"'+item.replace(/"/g, '""')+'"';
  }).join(',')+'\n');

  if (node.children && node.children.length) {
    node.children.forEach(function (childnode) {
      csvbuff = displayCSVNode(childnode, csvbuff, family);
    });
  }

  return csvbuff;
}

data.children.forEach(function (node) {
  buff = displayNode(node, buff);
});

document.getElementById('content').innerHTML = buff.join('');

data.children.forEach(function (node) {
  csvbuff = displayCSVNode(node, csvbuff);
});

document.getElementById('csvcontent').innerHTML = csvbuff.join('');


var nodes = [].slice.call(document.querySelectorAll('li'));
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("handle")) {
    e.stopPropagation();
    e.target.parentNode.classList.toggle("selected");
  }
});
document.getElementById("btn_open_all").addEventListener("click", function (e) {
  nodes.forEach(function (node) {
    node.classList.add("selected");
  }); 
});
document.getElementById("btn_close_all").addEventListener("click", function (e) {
  nodes.forEach(function (node) {
    node.classList.remove("selected");
  }); 
});
document.getElementById("btn_toggle_empty").addEventListener("click", function (e) {
  document.body.classList.toggle("noempty");
});
document.getElementById("btn_download_csv").addEventListener("click", function (e) {
  this.href = 'data:application/csv;charset=utf-8,'+encodeURIComponent(document.getElementById('csvcontent').childNodes[0].nodeValue);
});


var margin = 20,
    diameter = 960;

var color = d3.scale.linear()
    .domain([-1, 5])
    .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
    .interpolate(d3.interpolateHcl);

var pack = d3.layout.pack()
    .padding(2)
    .size([diameter - margin, diameter - margin])
    .value(function(d) { return d.countAll; })

var svg = d3.select("body").append("svg")
    .attr("width", diameter)
    .attr("height", diameter)
  .append("g")
    .attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");

var focus = data,
    root = data,
    nodes = pack.nodes(data),
    view;

var circle = svg.selectAll("circle")
    .data(nodes)
  .enter().append("circle")
    .attr("class", function(d) { return d.parent ? d.children ? "node" : "node node--leaf" : "node node--root"; })
    .style("fill", function(d) { return d.children ? color(d.depth) : null; })
    .on("click", function(d) { if (focus !== d) zoom(d), d3.event.stopPropagation(); });

var text = svg.selectAll("text")
    .data(nodes)
  .enter().append("text")
    .attr("class", "label")
    .style("fill-opacity", function(d) { return d.parent === root ? 1 : 0; })
    .style("display", function(d) { return d.parent === root ? null : "none"; })
    .text(function(d) { return d.name; });

var node = svg.selectAll("circle,text");

zoomTo([root.x, root.y, root.r * 2 + margin]);

function zoom(d) {
  var focus0 = focus; focus = d;

  var transition = d3.transition()
      .duration(d3.event.altKey ? 7500 : 750)
      .tween("zoom", function(d) {
        var i = d3.interpolateZoom(view, [focus.x, focus.y, focus.r * 2 + margin]);
        return function(t) { zoomTo(i(t)); };
      });

  transition.selectAll("text")
    .filter(function(d) { return d.parent === focus || this.style.display === "inline"; })
      .style("fill-opacity", function(d) { return d.parent === focus ? 1 : 0; })
      .each("start", function(d) { if (d.parent === focus) this.style.display = "inline"; })
      .each("end", function(d) { if (d.parent !== focus) this.style.display = "none"; });
}

function zoomTo(v) {
  var k = diameter / v[2]; view = v;
  node.attr("transform", function(d) { return "translate(" + (d.x - v[0]) * k + "," + (d.y - v[1]) * k + ")"; });
  circle.attr("r", function(d) { return d.r * k; });
}

d3.select(self.frameElement).style("height", diameter + "px");