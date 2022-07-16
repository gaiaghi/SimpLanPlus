push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
b label_0
function_0:
mv $fp $sp
push $ra
mv $al $fp
lw $a0 1($al)
lw $a0 0($a0)
lw $a0 0($a0)
print $a0 1
label_1:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_2
lw $a0 0($sp)
pop
b label_3
label_2:
lw $a0 0($sp)
pop
b function_1
label_3:
function_1:
lw $ra 0($sp)
addi $sp $sp 3
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
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
lw $a0 -3($al)
push $a0
mv $al $fp
addi $a0 $al -2
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 2
push $a0
mv $al $fp
addi $a0 $al -2
lw $a0 0($a0)
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
push $fp
mv $al $fp
lw $a0 -2($al)
push $a0
mv $al $fp
push $al
jal function_0
mv $al $fp
lw $a0 -3($al)
del $a0
mv $al $fp
lw $a0 -2($al)
lw $a0 0($a0)
lw $a0 0($a0)
print $a0 1
addi $sp $sp 2
pop
pop
lw $fp 0($sp)
pop
halt
