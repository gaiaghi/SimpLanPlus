push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
b label_145
function_122:
mv $fp $sp
push $ra
mv $al $fp
lw $a0 1($al)
push $a0
mv $al $fp
lw $a0 2($al)
lw $t1 0($sp)
bleq $t1 $a0 label_151
li $a0 1
b label_152
label_151:
li $a0 0
label_152:
pop
li $t1 1
beq $a0 $t1 label_150
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
jal function_122
label_148:
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_153
lw $a0 0($sp)
pop
b label_154
label_153:
lw $a0 0($sp)
pop
b label_146
label_154:
b label_149
label_150:
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
label_147:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_155
lw $a0 0($sp)
pop
b label_156
label_155:
lw $a0 0($sp)
pop
b label_146
label_156:
label_149:
label_146:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_157
lw $a0 0($sp)
pop
b label_158
label_157:
lw $a0 0($sp)
pop
b function_123
label_158:
function_123:
lw $ra 0($sp)
addi $sp $sp 4
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_145:
push $fp
li $a0 5
push $a0
li $a0 4
push $a0
mv $al $fp
push $al
jal function_122
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
