************************************************************************
file with basedata            : mm27_.bas
initial value random generator: 1716516702
************************************************************************
projects                      :  1
jobs (incl. supersource/sink ):  12
horizon                       :  71
RESOURCES
  - renewable                 :  2   R
  - nonrenewable              :  2   N
  - doubly constrained        :  0   D
************************************************************************
PROJECT INFORMATION:
pronr.  #jobs rel.date duedate tardcost  MPM-Time
    1     10      0       10        6       10
************************************************************************
PRECEDENCE RELATIONS:
jobnr.    #modes  #successors   successors
   1        1          3           2   3   4
   2        3          1           8
   3        3          2           6   7
   4        3          3           5   6  11
   5        3          2           7  10
   6        3          2           8   9
   7        3          1           9
   8        3          1          10
   9        3          1          12
  10        3          1          12
  11        3          1          12
  12        1          0        
************************************************************************
REQUESTS/DURATIONS:
jobnr. mode duration  R 1  R 2  N 1  N 2
------------------------------------------------------------------------
  1      1     0       0    0    0    0
  2      1     2       0    3    9    0
         2     3       8    0    8    0
         3    10       6    0    8    0
  3      1     2       5    0    0    4
         2     4       4    0    3    0
         3    10       0   10    0    3
  4      1     3       8    0    5    0
         2     4       0    9    4    0
         3    10       0    8    4    0
  5      1     1       0    2    0    7
         2     1       0    7    8    0
         3     3       6    0    0    9
  6      1     3       4    0    0   10
         2     4       3    0    0    7
         3     5       2    0    3    0
  7      1     1       0    4    0   10
         2     6       6    0   10    0
         3     7       1    0    0    7
  8      1     2       9    0    0    9
         2     4       7    0    5    0
         3     6       2    0    0    4
  9      1     2       0    7    7    0
         2     4       0    4    0    5
         3     8       4    0    4    0
 10      1     2       0    3    9    0
         2     2       0    3    0    4
         3     9       8    0    7    0
 11      1     1       0    8    1    0
         2     3       4    0    0    4
         3     3       0    8    0    2
 12      1     0       0    0    0    0
************************************************************************
RESOURCEAVAILABILITIES:
  R 1  R 2  N 1  N 2
   21   25   46   39
************************************************************************