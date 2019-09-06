lui $v1, 61440
addi $s4, $zero, 63
lui $t0, 32768
add $a0, $v1, $v1
addi $v0, $zero, 1
nor $at, $zero, $zero
add $t2, $at, $zero
addi $a3, $zero, 3
nor $a3, $a3, $a3
addi $a2, $zero, 32767
add $s1, $zero, $zero
addi $a1, $zero, 683
sw $a1, ($v1)
addi $s2, $zero, 2
sw $zero, 4($v1)
lw $a1, ($v1)
add $a1, $a1, $a1
add $a1, $a1, $a1
sw $a1, ($v1)
sw $a2, 4($v1)
lui $t5, 65535
label_4:
lw $a1, ($v1)
add $a1, $a1, $a1
add $a1, $a1, $a1
sw $a1, ($v1)
lw $a1, ($v1)
and $t3, $a1, $t0
addi $t5, $t5, 1
beq $t3, $t0, label_0
label_10:
lw $a1, ($v1)
addi $s2, $zero, 24
and $t3, $a1, $s2
beq $t3, $zero, label_1
beq $t3, $s2, label_2
addi $s2, $zero, 8
beq $t3, $s2, label_3
sw $t1, ($a0)
j label_4
label_1:
beq $t2, $at, label_5
j label_6
label_5:
nor $t2, $zero, $zero
add $t2, $t2, $t2
label_6:
sw $t2, ($a0)
j label_4
label_2:
lw $t1, 672($s1)
sw $t1, ($a0)
j label_4
label_3:
lw $t1, 608($s1)
sw $t1, ($a0)
j label_4
label_0:
lui $t5, 65535
add $t2, $t2, $t2
or $t2, $t2, $v0
addi $s1, $s1, 4
and $s1, $s1, $s4
addi $t1, $t1, 1
beq $t1, $at, label_7
j label_8
label_7:
addi $t1, $t1, 5
label_8:
lw $a1, ($v1)
add $t3, $a1, $a1
add $t3, $t3, $t3
sw $t3, ($v1)
sw $a2, 4($v1)
label_9:
lw $a1, ($v1)
and $t3, $a1, $t0
beq $t3, $t0, label_9
j label_10
.space 304
.word 0xF0000000
sltu $zero, $zero, $zero
lb $zero, ($zero)
.word 0x3F
.word 0x1
.word 0xFFFF0000
.word 0xFFFF
lb $zero, ($zero)
.word 0x0
beq $t0, $s1, label_11
addi $v0, $s1, 8738
andi $s3, $t9, 13107
.word 0x44444444
bnel $t2, $s5, label_12
.word 0x66666666
.word 0x77777777
lwl $t0, 34952($a0)
lwr $t9, 39321($t4)
swl $t2, 43690($s5)
swr $k1, 48059($sp)
pref $t4, 52428($a2)
.word 0xDDDDDDDD
swc3 $t6, 61166($s7)
.word 0xFFFFFFFF
bnel $t3, $fp, label_13
ldc1 $sp, 64473($sp)
ldc1 $k1, 64953($fp)
.word 0xDFCFFCFB
.word 0xDFCFBFFF
sdc1 $s3, 57343($ra)
.word 0xFFFFDF3D
.word 0xFFFF9DB9
.word 0xFFFFBCFB
.word 0xDFCFFCFB
.word 0xDFCFBFFF
ldc1 $k1, 40959($fp)
ldc1 $k1, 64953($fp)
ldc1 $sp, 64473($sp)
.word 0xFFFF07E0
.word 0x7E0FFF
add $fp, $sp, $sp
add $ra, $fp, $fp
j label_14
