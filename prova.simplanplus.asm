-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- dec 1
-------------------- inizio decFun
function_0:
mv $fp $sp
push $ra
-------------------- inizio decFun.block
-------------------- inizio blocco
-------------------- inizio decs
-------------------- inizio stms
-------------------- stm 1
lw $al 0($fp)
lw $a0 1($al)
print $a0
-------------------- fine stms
label_0:
-------------------- pulizia blocco
addi $sp $sp 0
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_1
lw $a0 0($sp)
pop
b label_2
label_1:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b function_1
label_2:
-------------------- fine blocco
-------------------- pulizia decFun
function_1:
lw $ra 0($sp)
addi $sp $sp 2
lw $fp 0($sp)
pop
li $ret 0
jr $ra
-------------------- fine decFun
-------------------- dec 2
li $a0 3
push $a0
-------------------- inizio stms
-------------------- stm 1
-------------------- inizio call
push $fp
lw $al 0($fp)
lw $a0 -3($al)
push $a0
mv $al $fp
push $al
jal function_0
-------------------- fine call
-------------------- fine stms
-------------------- pulizia blocco
addi $sp $sp 1
pop
pop
lw $fp 0($sp)
pop
halt
-------------------- fine blocco
