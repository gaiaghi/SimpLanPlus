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
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
lw $a0 -2($al)
lw $a0 0($a0)
push $a0
li $a0 0
lw $t1 0($sp)
beq $t1 $a0 label_4
li $a0 0
b label_5
label_4:
li $a0 1
label_5:
pop
li $t1 1
beq $a0 $t1 label_3
b label_2
label_3:
li $a0 1
print $a0 1
label_2:
li $a0 2
li $ret 1
b label_1
label_1:
addi $sp $sp 1
push $a0
li $a0 1
beq $ret $a0 label_6
lw $a0 0($sp)
pop
b label_7
label_6:
lw $a0 0($sp)
pop
b function_1
label_7:
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
