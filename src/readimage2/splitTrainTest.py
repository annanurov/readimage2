import random


def file2lines(filename):
  f = open(filename, "r")
  lines = f.readlines()
  f.close()
  return lines


def makeRandomArray(ones, zeros):
  res = [1] * ones
  res += [0] * zeros
  N = ones + zeros -1
  for i in range(N):
    j = random.randint(i+1, N)
    a = res[i]
    res[i] = res[j]
    res[j] = a
  return res
##--end of makeRandomArray


for i in range(3):
  print (makeRandomArray(10,2))
  
filename = "resized.csv"
lines = file2lines(filename)
training_lines = []
training_lines_file_name = "resized_training.cvs"
testing_lines = []
test_lines_file_name = "resized_test.cvs"
training_lines_file = open(training_lines_file_name, "w")
test_lines_file = open(test_lines_file_name, "w")
header = lines[0]

[n, m] = header.split(",")[:2]
x = len(n)+len(m)
n = int(n)
zeros = n/10
print (str(n))
print (zeros  )

a = makeRandomArray(n - zeros , zeros)

training_lines_file.write(str(n - zeros)+ ", " + m + ", " + header[(x+2):])
test_lines_file.write(str(zeros)+ ", " + m + ", " + header[(x+2):])

for i in range(n):
  if a[i]:
#    training_lines.append(lines[i+1])
    training_lines_file.write(lines[i+1])
  else:
#    testing_lines.append(lines[i+1])
    test_lines_file.write(lines[i+1])


training_lines_file.close()
test_lines_file.close()


  
  
  
  
  
  
  
  
  
  
  
  
  

