push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
b label_163
function_108:
mv $fp $sp
push $ra
mv $al $fp
lw $a0 1($al)
push $a0
mv $al $fp
lw $a0 2($al)
lw $t1 0($sp)
bleq $t1 $a0 label_169
li $a0 1
b label_170
label_169:
li $a0 0
label_170:
pop
li $t1 1
beq $a0 $t1 label_168
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
jal function_108
label_166:
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_171
lw $a0 0($sp)
pop
b label_172
label_171:
lw $a0 0($sp)
pop
b label_164
label_172:
b label_167
label_168:
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
label_165:
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
push $a0
li $a0 1
beq $ret $a0 label_173
lw $a0 0($sp)
pop
b label_174
label_173:
lw $a0 0($sp)
pop
b label_164
label_174:
label_167:
label_164:
addi $sp $sp 0
push $a0
li $a0 1
beq $ret $a0 label_175
lw $a0 0($sp)
pop
b label_176
label_175:
lw $a0 0($sp)
pop
b function_109
label_176:
function_109:
lw $ra 0($sp)
addi $sp $sp 4
lw $fp 0($sp)
pop
li $ret 0
jr $ra
label_163:
push $fp
li $a0 5
push $a0
li $a0 4
push $a0
mv $al $fp
push $al
jal function_108
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
