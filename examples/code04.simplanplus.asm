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
lw $a0 2($al)
lw $a0 0($a0)
push $a0
li $a0 0
lw $t1 0($sp)
beq $t1 $a0 label_5
li $a0 0
b label_6
label_5:
li $a0 1
label_6:
pop
li $t1 1
beq $a0 $t1 label_4
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
mv $al $fp
lw $al 0($al)
lw $a0 1($al)
lw $a0 0($a0)
push $a0
li $a0 1
lw $t1 0($sp)
sub $a0 $t1 $a0
pop
push $a0
mv $al $fp
lw $al 0($al)
addi $a0 $al 1
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
push $fp
mv $al $fp
lw $al 0($al)
lw $a0 1($al)
push $a0
mv $al $fp
lw $al 0($al)
lw $a0 2($al)
push $a0
mv $al $fp
push $al
jal function_0
label_2:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_7
lw $a0 0($sp)
pop
b label_8
label_7:
lw $a0 0($sp)
pop
b label_1
label_8:
b label_3
label_4:
mv $al $fp
lw $a0 1($al)
push $a0
lw $a0 0($a0)
lw $a0 0($sp)
pop
del $a0
label_3:
label_1:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_9
lw $a0 0($sp)
pop
b label_10
label_9:
lw $a0 0($sp)
pop
b function_1
label_10:
function_1:
lw $ra 0($sp)
addi $sp $sp 4
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
