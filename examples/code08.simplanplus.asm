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
mv $al $fp
lw $a0 2($al)
lw $t1 0($sp)
bleq $t1 $a0 label_6
li $a0 1
b label_7
label_6:
li $a0 0
label_7:
pop
li $t1 1
beq $a0 $t1 label_5
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
li $a0 1
push $a0
push $fp
mv $al $fp
lw $al 0($al)
lw $a0 2($al)
push $a0
li $a0 1
lw $t1 0($sp)
add $a0 $t1 $a0
pop
push $a0
mv $al $fp
lw $al 0($al)
lw $a0 1($al)
push $a0
li $a0 1
lw $t1 0($sp)
add $a0 $t1 $a0
pop
push $a0
mv $al $fp
push $al
jal function_0
label_3:
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_8
lw $a0 0($sp)
pop
b label_9
label_8:
lw $a0 0($sp)
pop
b label_1
label_9:
b label_4
label_5:
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
mv $al $fp
lw $al 0($al)
lw $a0 1($al)
push $a0
mv $al $fp
lw $al 0($al)
lw $a0 2($al)
lw $t1 0($sp)
add $a0 $t1 $a0
pop
print $a0 1
label_2:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_10
lw $a0 0($sp)
pop
b label_11
label_10:
lw $a0 0($sp)
pop
b label_1
label_11:
label_4:
label_1:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_12
lw $a0 0($sp)
pop
b label_13
label_12:
lw $a0 0($sp)
pop
b function_1
label_13:
function_1:
lw $ra 0($sp)
addi $sp $sp 4
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
push $fp
li $a0 5
push $a0
li $a0 4
push $a0
mv $al $fp
push $al
jal function_0
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
