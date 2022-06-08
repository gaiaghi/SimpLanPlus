-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- inizio decFun
function_0:
mv $fp $sp
push $ra
-------------------- inizio decFun.block
-------------------- inizio blocco
-------------------- inizio decs
-------------------- inizio stms
-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
addi $sp $sp -1
-------------------- inizio stms
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
push $a0
li $a0 0
lw $t1 0($sp)
beq $t1 $a0 label_3
li $a0 0
b label_4
label_3:
li $a0 1
label_4:
pop
li $t1 1
beq $a0 $t1 label_2
lw $al 0($fp)
lw $al 0($al)
lw $a0 1($al)
print $a0
b label_1
label_2:
b null
label_1:
label_0:
-------------------- pulizia blocco
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
-------------------- fine blocco
lw $al 0($fp)
lw $a0 2($al)
print $a0
function_1:
-------------------- pulizia blocco
addi $sp $sp 0
-------------------- fine blocco
-------------------- pulizia decFun
lw $ra 0($sp)
addi $sp $sp 3
lw $fp 0($sp)
pop
jr $ra
-------------------- fine decFun
-------------------- inizio stms
-------------------- pulizia blocco
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
-------------------- fine blocco
