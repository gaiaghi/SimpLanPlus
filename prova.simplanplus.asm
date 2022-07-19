push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
b label_159
function_122:
mv $fp $sp
push $ra
mv $al $fp
lw $a0 1($al)
push $a0
mv $al $fp
lw $a0 2($al)
lw $t1 0($sp)
bleq $t1 $a0 label_165
li $a0 1
b label_166
label_165:
li $a0 0
label_166:
pop
li $t1 1
beq $a0 $t1 label_164
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
label_162:
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_167
lw $a0 0($sp)
pop
b label_168
label_167:
lw $a0 0($sp)
pop
b label_160
label_168:
b label_163
label_164:
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
label_161:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_169
lw $a0 0($sp)
pop
b label_170
label_169:
lw $a0 0($sp)
pop
b label_160
label_170:
label_163:
label_160:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_171
lw $a0 0($sp)
pop
b label_172
label_171:
lw $a0 0($sp)
pop
b function_123
label_172:
function_123:
lw $ra 0($sp)
addi $sp $sp 4
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_159:
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
