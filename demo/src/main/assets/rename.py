#/bin/python
import os
import re

file_list = os.listdir(".")
p = re.compile(r"(plant_\d_\d_\w_\d)_(\d)(_anim.+)")

for f in file_list:
    m = p.match(f)
    if m:
        index = int(m.group(2))
        pre_f = p.sub('\\1_'+str(index-1)+'\\3', f)
        next_f = p.sub('\\1_'+str(index+1)+'\\3', f)
        rename_to = p.sub('\\1\\3', f)
        if (next_f not in file_list) and (pre_f not in file_list):
            print "trying to rename %s to %s" % (f, rename_to)
            os.rename(f, rename_to)
            print "renamed %s to %s" % (f, rename_to)
