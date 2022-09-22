push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
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
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
addi $sp $sp -1
mv $al $fp
lw $al 0($al)
lw $a0 -2($al)
lw $a0 0($a0)
push $a0
mv $al $fp
addi $a0 $al -2
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 -2($al)
print $a0 1
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
mv $al $fp
lw $a0 -2($al)
print $a0 1
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
