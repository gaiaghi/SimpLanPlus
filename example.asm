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
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
