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
li $a0 1
print $a0
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
li $ret 1
b label_2
label_2:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_3
lw $a0 0($sp)
pop
b label_4
label_3:
lw $a0 0($sp)
pop
b label_1
label_4:
label_1:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_5
lw $a0 0($sp)
pop
b label_6
label_5:
lw $a0 0($sp)
pop
b function_1
label_6:
function_1:
lw $ra 0($sp)
addi $sp $sp 2
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
push $fp
mv $al $fp
push $al
jal function_0
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
