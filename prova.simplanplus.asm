push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
function_0:
mv $fp $sp
push $ra
lw $al 0($fp)
lw $a0 2($al)
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
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
lw $a0 0($a0)
push $a0
li $a0 1
lw $t1 0($sp)
sub $a0 $t1 $a0
pop
push $a0
lw $al 0($fp)
lw $al 0($al)
addi $a0 $al 1
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
push $fp
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
push $a0
lw $al 0($fp)
lw $al 0($al)
lw $a0 2($al)
push $a0
mv $al $fp
push $al
jal function_0
label_1:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_6
lw $a0 0($sp)
pop
b label_7
label_6:
lw $a0 0($sp)
pop
b label_0
label_7:
b label_2
label_3:
lw $al 0($fp)
lw $a0 1($al)
del $a0
label_2:
label_0:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_8
lw $a0 0($sp)
pop
b label_9
label_8:
lw $a0 0($sp)
pop
b function_1
label_9:
function_1:
lw $ra 0($sp)
addi $sp $sp 3
lw $fp 0($sp)
pop
li $ret 0
jr $ra
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
