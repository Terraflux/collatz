#!/usr/bin/python3
import sys
import time
from ete3 import Tree, TreeStyle, NodeStyle, TextFace
from multiprocessing import Pool

#Global Parameters
maxDepth = int(sys.argv[1])
nodeList = []
auxList = []
visited = [0, 1]
currentDepth = 1
#Set Growth Node Style
ns1 = NodeStyle()
ns1["fgcolor"] = "blue"
ns1["size"] = 5
#Set Reduction Node Style
ns2 = NodeStyle()
ns2["fgcolor"] = "red"
ns2["size"] = 5
#Set Convergence Node Style
ns3 = NodeStyle()
ns3["fgcolor"] = "orange"
ns3["size"] = 10

def grow():
    global nodeList
    global auxList
    global visited
    global currentDepth
    global maxDepth
    while (nodeList and currentDepth < maxDepth):
        currentDepth+=1
        print("Current Depth: " + str(currentDepth))
        newNodes = list(p.map(rCol, nodeList)) #PARALLEL OPERATION
        for node in nodeList:
            visited.append(int(node.name))
            for newNodeGroup in newNodes: #Link & Remove Nodes
                if (int(newNodeGroup[0].name) == int(node.name)*2):
                    node.add_child(newNodeGroup[0])
                    auxList.append(newNodeGroup[0])                
                    if (len(newNodeGroup) == 2 and int(newNodeGroup[0].name) not in visited):
                        node.add_child(newNodeGroup[1])
                        auxList.append(newNodeGroup[1])    
                    newNodes.remove(newNodeGroup)
                    break
                
        nodeList = list(auxList)
        auxList = []       

def rCol(currentNode): #Collatz Enumeration
    nodeArr = []
    val = int(currentNode.name)*2 #Growth Mode
    if(val not in visited):
        nodeArr.append(proc(currentNode, val, currentNode.depth+1, ns1))
    if ((int(currentNode.name) - 1) % 3 == 0): #Reduction Mode Check
        val = ((int(currentNode.name) - 1) / 3)
        if(val not in visited):
            nodeArr.append(proc(currentNode, val, currentNode.depth+1, ns2))
    return nodeArr

def proc(currentNode, newVal, depth, style):
    nextNode = Tree(name='{}'.format(newVal))
    nextNode.add_feature("depth", depth)
    nextNode.set_style(style)
    nextNode.add_face(TextFace(nextNode.name), column=0)
    return nextNode

t = Tree(name='1')
t.add_feature("depth", 0)
tb = t.add_child(name='2')
tb.add_feature("depth", 1)
nodeList.append(tb)
start = time.time()
p = Pool(8)
grow()
print(t)
print("Time Computing: " + str(time.time() - start))

start = time.time()
#ts = TreeStyle()
#ts.mode = 'c'
#ts.show_leaf_name = False
#t.render('colpic{}.png'.format(maxDepth), tree_style=ts)

t.write(outfile='newick{}.txt'.format(maxDepth))
print("Time Writing: " + str(time.time() - start))
