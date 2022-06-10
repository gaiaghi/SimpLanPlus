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
li $a0 1
print $a0
-------------------- stm 2
-------------------- inizio blocco
push $fp
mv $al $fp
push $al
mv $fp $sp
li $t1 0
push $t1
-------------------- inizio decs
-------------------- inizio stms
-------------------- stm 1
-------------------- RET
li $ret 1
b label_1
-------------------- fine stms
label_1:
-------------------- pulizia blocco
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_2
lw $a0 0($sp)
pop
b label_3
label_2:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b label_0
label_3:
-------------------- fine blocco
-------------------- fine stms
label_0:
-------------------- pulizia blocco
addi $sp $sp 0
-------------------- controllo se c'è stato un return
push $a0
li $a0 1
beq $ret $a0 label_4
lw $a0 0($sp)
pop
b label_5
label_4:
lw $a0 0($sp)
pop
-------------------- se $ret == 1 vai a
b function_1
label_5:
-------------------- fine blocco
-------------------- pulizia decFun
function_1:
lw $ra 0($sp)
addi $sp $sp 1
lw $fp 0($sp)
pop
li $ret 0
jr $ra
-------------------- fine decFun
-------------------- inizio stms
-------------------- fine stms
-------------------- pulizia blocco
addi $sp $sp 0
pop
pop
lw $fp 0($sp)
pop
halt
-------------------- fine blocco
