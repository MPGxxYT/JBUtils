package me.mortaldev.jbutils.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ChanceMapTest {

  @Test
  void size() {
    ChanceMap<String> chanceMap = new ChanceMap<>();
    assertEquals(0, chanceMap.size());
    chanceMap.put("test", false);
    assertEquals(1, chanceMap.size());
  }

  @Test
  void roll() {
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test");
    assertEquals("test", chanceMap.roll());
  }

  @Test
  void updateKey() {
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test");
    chanceMap.updateKey("test", 50);
    assertEquals(50, chanceMap.getTable().get("test").intValue());
  }

  @Test
  void remove() {
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test");
    assertEquals(1, chanceMap.size());
    chanceMap.remove("test");
    assertEquals(0, chanceMap.size());
  }

  @Test
  void getTable(){
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test");
    assertTrue(chanceMap.getTable().containsKey("test"));
  }

  @Test
  void setTable() {
    LinkedHashMap<String, BigDecimal> replaceTable = new LinkedHashMap<>(){{
      put("test2", new BigDecimal(50));
    }};
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test");
    assertTrue(chanceMap.getTable().containsKey("test"));
    chanceMap.setTable(replaceTable);
    assertTrue(chanceMap.getTable().containsKey("test2"));
    assertFalse(chanceMap.getTable().containsKey("test"));
  }

  @Test
  void isBalancedAndBalanceTable(){
    ChanceMap<String> chanceMap = new ChanceMap<>();
    chanceMap.put("test", 50);
    chanceMap.put("test2", 60);
    assertEquals(new BigDecimal("110.00"), chanceMap.getTotal());
    assertFalse(chanceMap.isBalanced());
    chanceMap.balanceTable();
    assertTrue(chanceMap.isBalanced());
  }



}
