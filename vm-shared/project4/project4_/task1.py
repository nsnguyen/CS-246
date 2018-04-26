#!/usr/bin/env python3

import sys
import os
import numpy
import numpy.linalg
import scipy.misc # version 0.17.0



def getOutputPngName(path, rank):
    filename, ext = os.path.splitext(path)
    return filename + '.' + str(rank) + '.png'

def getOutputNpyName(path, rank):
    filename, ext = os.path.splitext(path)
    return filename + '.' + str(rank) + '.npy'



if len(sys.argv) < 3:
    sys.exit('usage: task1.py <PNG inputFile> <rank>')

inputfile = sys.argv[1]
rank = int(sys.argv[2])
outputpng = getOutputPngName(inputfile, rank)
outputnpy = getOutputNpyName(inputfile, rank)

#
# TODO: The current code just prints out what it is supposed to to
#       Replace the print statement wth your code
#
# 1.Load an image file (whose name is provided as the first command-line argument) into a numpy array using the scipy.misc.imread function.
imread_output = scipy.misc.imread(inputfile)

# 2.Perform singular value decomposition on the array
# http://theory.stanford.edu/~tim/s15/l/l9.pdf
U, s, V = numpy.linalg.svd(imread_output, full_matrices=False)

# 3.Keep only the top-k entries in the SVD decomposed matrices and 
# multiply them to obtain the best rank-k approximation of the original array 
# (the k value should come from the second command-line argument of task1.py)


# https://docs.scipy.org/doc/numpy-1.13.0/reference/generated/numpy.linalg.svd.html

#numpy array is stored in [rows,columns]
V_rank = V[0:rank, :] # 
U_rank = U[:, 0:rank]
s_rank = s[0:rank]
S = numpy.diag(s_rank) #need to make matrix diagonal
result = numpy.dot(U_rank, numpy.dot(S,V_rank)) #reconstructing SVD matrix with new top-k entries

# Save the approximated array as 
# (1) a binary array file (with the name from the getOutputNpyName function call) using numpy.save function and 
# (2) a PNG file (with the name from the getOutputPngName function call) using the scipy.misc.imsave function.
numpy.save(file=outputnpy, arr=result)
scipy.misc.imsave(name=outputpng,arr=result)

