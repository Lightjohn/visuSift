# visuSift

This small app is intended to plot the links between two points. These points can be the results of Sift keypoints matching algorithm.
This repository is ready to be used in Eclipse.

# Usage

the app takes two parameters: the image and the text file.

# About the text file

The matchings points should be as follow: first line is the number of points and then come the points.

```
4
463.605  1846.44  1432.73  1095.68
923.962  2015.14  1903.14  1223.2
254.604  1387.61  1186.44  647.529
1047.3  1385.81  1975.55  580.776
```

For now, the points are separated by two spaces.

# Possible evolutions

* the number of links plotted is internal, make it external
* Color is also an internal parameter
* Reload option
* No redimension
* Text file: points are separated by two spaces: not standard.

# Example

![Alt text](example.png?raw=true "Example")