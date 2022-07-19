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
b label_4
function_2:
mv $fp $sp
push $ra
label_5:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_6
lw $a0 0($sp)
pop
b label_7
label_6:
lw $a0 0($sp)
pop
b function_3
label_7:
function_3:
lw $ra 0($sp)
addi $sp $sp 3
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_4:
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
push $fp
mv $al $fp
lw $a0 -2($al)
push $a0
mv $al $fp
push $al
jal function_2
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
