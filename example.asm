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
push $a0
li $a0 1
lw $t1 0($sp)
add $a0 $t1 $a0
pop
li $ret 1
b label_1
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
push $fp
mv $al $fp
lw $a0 1($al)
push $a0
mv $al $fp
push $al
jal function_0
li $ret 1
b label_5
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
li $a0 1
push $a0
mv $al $fp
lw $a0 -2($al)
push $a0
push $fp
li $a0 1
push $a0
mv $al $fp
push $al
jal function_0
lw $t1 0($sp)
beq $t1 $a0 label_10
li $a0 0
b label_11
label_10:
li $a0 1
label_11:
pop
li $t1 1
beq $a0 $t1 label_9
push $fp
li $a0 3
push $a0
mv $al $fp
push $al
jal function_2
push $a0
mv $al $fp
addi $a0 $al -2
lw $t1 0($sp)
pop
sw $t1 0($a0)
b label_8
label_9:
push $fp
li $a0 2
push $a0
mv $al $fp
push $al
jal function_0
push $a0
mv $al $fp
addi $a0 $al -2
lw $t1 0($sp)
pop
sw $t1 0($a0)
label_8:
mv $al $fp
lw $a0 -2($al)
print $a0 1
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
