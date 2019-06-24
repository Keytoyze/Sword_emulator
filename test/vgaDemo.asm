li $s0, 0xC2000; // vram
li $s1, 0xFFFFFF04; // counter
li $s2, 0xFFFFFF00; // gpio
begin:
  li $t1, 0; // y
  move $t0, $s0; // offset

  loop1:

    slti $t3, $t1, 480; // t3 = y < 480
    beq $t3, $zero, end1;

    li $t2, 0; //x

    lw $a0, 0($s2);
    slt $a0, $a0, $zero;
    beq $a0, $zero, loop2;
    li $t3, 256000;
    xori $a3, $a3, 1;
    sw $t3, 0($s1);

    loop2:
      slti $t3, $t2, 640; // t3 = x < 640
      beq $t3, $zero, end2;

      slti $t3, $t1, 200; // t3 = y < 200
      slti $t4, $t2, 200; // t4 = x < 200

      and $t5, $t3, $t4; // t5 = t3 && t4
      xor $t5, $t5, $a3;
	 beq $t5, $zero, blue;

	 addiu $a1, $zero, 0b11110000;
	 j exit;

      blue:
	   addiu $a1, $zero, 0b1111;

      exit:
	   sh $a1, 0($t0);
      addi $t0, $t0, 2; // offset += 2
      addi $t2, $t2, 1; // x++
      j loop2;
    end2:
      addi $t1, $t1, 1; // y++
      j loop1;
  end1:
    j begin;
