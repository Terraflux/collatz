#!/usr/bin/python3
import sys
import time
from collections import deque
from ete3 import Tree, TreeStyle, NodeStyle, TextFace, faces
from multiprocessing import Pool



#Global Parameters
maxDepth = int(sys.argv[1])
nodeQueue = deque([])
auxQueue = deque([])
visited = [0, 1]
currentDepth = 1
p = Pool(8)
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

class colTree(Tree):

    def grow(self):
        global nodeQueue
        while (nodeQueue):
            #print(nodeQueue)
            #self.rCol(nodeQueue.popleft())
            p.map(self.rCol, nodeQueue)
            if (not nodeQueue):
                nodeQueue = auxQueue
                auxQueue.clear()

    def proc(self, currentNode, newVal, depth, style):
        global currentDepth
        nextNode = colTree(name='{}'.format(newVal))
        nextNode.add_feature("depth", depth)
        nextNode.set_style(style)
        nextNode.add_face(TextFace(nextNode.name), column=0)
        currentNode.add_child(nextNode)
        if depth > currentDepth:
            currentDepth = nextNode.depth
            print(currentDepth)
        if depth < maxDepth:
            auxQueue.append(nextNode)

    def rCol(self, currentNode): #Main Collatz Enumeration
        visited.append(int(currentNode.name))
        val = int(currentNode.name)*2
        if(val not in visited):
            self.proc(currentNode, val, currentNode.depth+1, ns1)
        else:
            currentNode.set_style(ns3)

        if ((int(currentNode.name) - 1) % 3 == 0):
            val = ((int(currentNode.name) - 1) / 3)
            if(val not in visited):
                self.proc(currentNode, val, currentNode.depth+1, ns2)
            else:
                currentNode.set_style(ns3)


t = colTree(name='1')
t.add_feature("depth", 0)
tb = t.add_child(name='2')
tb.add_feature("depth", 1)
nodeQueue.append(tb)
start = time.time()
t.grow()
print("Time Computing: " + str(time.time() - start))

#ts = TreeStyle()
#ts.mode = 'c'
#ts.show_leaf_name = False
#t.render('colpic{}.png'.format(maxDepth), tree_style=ts)

start = time.time()
t.write(outfile='newick{}.txt'.format(maxDepth))
print("Time Writing: " + str(time.time() - start))
