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
addi $a0 $al 2
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 14
push $a0
mv $al $fp
addi $a0 $al 2
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al 1
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al 1
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 28
push $a0
mv $al $fp
addi $a0 $al 1
lw $a0 0($a0)
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
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
lw $t1 1($fp)
sw $t1 5($fp)
lw $t1 2($fp)
sw $t1 6($fp)
lw $ra 0($sp)
addi $sp $sp 5
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_0:
b label_4
function_2:
mv $fp $sp
push $ra
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al 2
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 3
push $a0
mv $al $fp
addi $a0 $al 2
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al 3
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al 3
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 25
push $a0
mv $al $fp
addi $a0 $al 3
lw $a0 0($a0)
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 2($al)
push $a0
mv $al $fp
lw $a0 3($al)
push $a0
push $fp
mv $al $fp
lw $a0 1($al)
push $a0
mv $al $fp
lw $a0 2($al)
push $a0
mv $al $fp
lw $a0 3($al)
push $a0
mv $al $fp
push $al
jal function_0
lw $t1 0($sp)
pop
mv $al $fp
sw $t1 3($al)
lw $t1 0($sp)
pop
mv $al $fp
sw $t1 2($al)
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
lw $t1 2($fp)
sw $t1 5($fp)
lw $t1 3($fp)
sw $t1 6($fp)
lw $ra 0($sp)
addi $sp $sp 5
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_4:
li $a0 4
push $a0
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
li $a0 1
push $a0
mv $al $fp
addi $a0 $al -3
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $t1 -1
sw $t1 0($hp)
addi $a0 $hp 0
addi $hp $hp 1
push $a0
mv $al $fp
addi $a0 $al -4
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
li $a0 22
push $a0
mv $al $fp
addi $a0 $al -4
lw $a0 0($a0)
lw $a0 0($a0)
lw $t1 0($sp)
pop
sw $t1 0($a0)
mv $al $fp
lw $a0 -3($al)
print $a0 1
mv $al $fp
lw $a0 -3($al)
lw $a0 0($a0)
print $a0 1
li $a0 1
print $a0 0
mv $al $fp
lw $a0 -4($al)
print $a0 1
mv $al $fp
lw $a0 -4($al)
lw $a0 0($a0)
print $a0 1
mv $al $fp
lw $a0 -4($al)
lw $a0 0($a0)
lw $a0 0($a0)
print $a0 1
li $a0 0
print $a0 0
mv $al $fp
lw $a0 -4($al)
push $a0
mv $al $fp
lw $a0 -3($al)
push $a0
push $fp
mv $al $fp
lw $a0 -4($al)
push $a0
mv $al $fp
lw $a0 -3($al)
push $a0
mv $al $fp
lw $a0 -2($al)
push $a0
mv $al $fp
push $al
jal function_2
lw $t1 0($sp)
pop
mv $al $fp
sw $t1 -3($al)
lw $t1 0($sp)
pop
mv $al $fp
sw $t1 -4($al)
mv $al $fp
lw $a0 -3($al)
print $a0 1
mv $al $fp
lw $a0 -3($al)
lw $a0 0($a0)
print $a0 1
li $a0 1
print $a0 0
mv $al $fp
lw $a0 -4($al)
print $a0 1
mv $al $fp
lw $a0 -4($al)
lw $a0 0($a0)
print $a0 1
mv $al $fp
lw $a0 -4($al)
lw $a0 0($a0)
lw $a0 0($a0)
print $a0 1
addi $sp $sp 3
pop
pop
lw $fp 0($sp)
pop
halt
