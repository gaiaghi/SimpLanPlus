push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
addi $sp $sp -1
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al -2
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 -3($al)
push $a0
mv $al $fp
addi $a0 $al -2
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 22
push $a0
mv $al $fp
addi $a0 $al -3
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 -3($al)
print $a0 1
mv $al $fp
lw $a0 -3($al)
del $a0
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al -3
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 -3($al)
print $a0 1
addi $sp $sp 2
pop
pop
lw $fp 0($sp)
pop
halt
