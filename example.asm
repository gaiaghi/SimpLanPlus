push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
li $a0 1111
push $a0
b label_0
function_0:
mv $fp $sp
push $ra
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
addi $sp $sp 2
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
li $a0 22222
push $a0
mv $al $fp
lw $a0 -2($al)
print $a0 1
mv $al $fp
lw $a0 -3($al)
print $a0 1
addi $sp $sp 2
pop
pop
lw $fp 0($sp)
pop
halt
