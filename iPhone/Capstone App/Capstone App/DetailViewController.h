//
//  DetailViewController.h
//  Capstone App
//
//  Created by Blayne Kennedy on 11/17/12.
//  Copyright (c) 2012 Blayne Kennedy. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DetailViewController : UIViewController <UISplitViewControllerDelegate>

@property (strong, nonatomic) id detailItem;

@property (weak, nonatomic) IBOutlet UILabel *detailDescriptionLabel;
@end
