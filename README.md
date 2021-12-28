# PixPainter
Create by KKoishi_

My homepage:http://kkoishi-514.top

****

### A Java picture drawer
The way to draw picture is input code in the terminal(Actually, it is JTextArea)

Also,it supports script file,like this(create an undirected-graph):

Upgrade:2021/12/24 10:40
add kGraph file type.

now you can use ```node``` and ```direct``` to draw a directed graph faster.

you can check the syntax of the script by invoking```check```

![use the PixPainter to draw](./rua.png "A example of undirected-graph")
```pixScript
create 1000 1000 rua;
setFont null plain 30;
setcolor red;
drawcircle 500 50 50;drawcircle 340 200 50;
drawcircle 660 200 50;drawcircle 180 350 50;
drawcircle 500 350 50;
drawcircle 820 350 50;
drawcircle 820 600 50;
setColor green;
drawCircle 500 800 100;
setColor cyan;
draw -line 470 90 370 160;draw -line 530 90 630 160;
draw -line 310 240 210 310;draw -line 370 240 470 310;
draw -line 690 240 790 310;
setColOr Pink;
drawpline 5 180 400 500 700 500 400 500 700 340 250;
drawpline 4 500 700 820 400 500 700 770 600;
draw -line 550 350 770 350;
drawstring 500 50 start;
drawstring 340 200 a1;drawstring 660 200 a2;
drawstring 180 350 b1;drawstring 500 350 b2;
drawstring 820 350 b3;drawstring 820 600 rua~~;
drawstring 500 800 end;
draw -rect 1 1 999 999;
draw -rect 1 1 50 50;
export;
```
The way to run the script is input ```kShell [path]```
.And every single command need to be ended with ";".
You can use ```gcc [path]``` to trans a kgraph file to a kscript file.

Cautions:
* Some commands are not finished
* Now it only supports png/jpg format,maybe I will add more export format-supporting soon.
* You can use "list" to check all the commands.

commands:
***The finished ones are marked with "√"***
*    create [width] [height] [name]√
*    setfont [name:null] bold/plain/italic [size]√
*    setcolor [color]√
*    draw -circle/-line/-ellipse/-rect [x1] [y1] [x2] [y2]√
*    drawcircle [x] [y] [r]√
*    fill circle/-ellipse/-rect [x1] [y2] [x2] [y2]√
*    drawpline [amount-point] [x] [y]...√
*    drawrect [amount-point] [x] [y]...√
*    fillp [amount-point] [x] [y]...√
*    drawpoint [x] [y]√
*    exportformat jpg/png√
*    drawpic [x] [y] (Optional param:[w] [h]) [path]√
*    drawcurel [x1] [x2] [func] -> hard
*    drawefunc [-sin/-cos/-tan/-csc/-sec/-cot/-asin/-acos/-atan/-sqrt/-cube/-exp/-log/-ln/-square] [x1] [x2] [size-y] [dy=size/2]√
*    drawstring [x] [y] [string]√
*    help [-[command]]
*    changedir [dir]√
*    kshell [path]√
*    preview√
*    list√
*    exit√
*    settings
*    info√
*    logs√
*    logbrowse√
*    export√
*    gcc [kgraph file]√
*    get√
*    node -circle [x] [y] [r] [string]√
*    node -rect/-ellipse [x] [y] [w] [h] [string]√
*    direct [x1] [y1] [x2] [y2] [branch-len=20] [degree=30°]√
*    check [script path]

## kGraph file example
```kGraph
info:"1000,1000,Graph";
dir:"C:/Users/DELL/Desktop";
font:"null,plain,20";
color:"red";
circle:"500,50,50";
circle:"340,200,50";
circle:"660,200,50";
circle:"180,350,50";
circle:"500,350,50";
circle:"820,350,50";
circle:"820,600,50";
color:"green";
circle:"500,800,100";
color:"cyan";
line:"470,90,370,160" ;
line:"530,90,630,160" ;
line:"310,240,210,310";
line:"370,240,470,310";
line:"690,240,790,310";
color:"pink";
pline:"5,180,400,500,700,500,400,500,700,340,250";
pline:"4,500,700,820,400,500,700,770,600";
line:"550,350,770,350";
```

The result of compile
```kScript
create 1000 1000 Graph;
changedir C:/Users/DELL/Desktop;
setfont null plain 20;
setcolor red;
drawcircle 500 50 50;
drawcircle 340 200 50;
drawcircle 660 200 50;
drawcircle 180 350 50;
drawcircle 500 350 50;
drawcircle 820 350 50;
drawcircle 820 600 50;
setcolor green;
drawcircle 500 800 100;
setcolor cyan;
draw -line 470 90 370 160;
draw -line 530 90 630 160;
draw -line 310 240 210 310;
draw -line 370 240 470 310;
draw -line 690 240 790 310;
setcolor pink;
drawpline 5 180 400 500 700 500 400 500 700 340 250;
drawpline 4 500 700 820 400 500 700 770 600;
draw -line 550 350 770 350;
export;
```

## Display
![img.png](img.png)
